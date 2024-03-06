#!/usr/bin/env bash

ARCH=$(uname -m)
OS=$(uname -s)
usage() {
    echo "Usage: ${PROGRAM_NAME} command dirname"
    echo "command: [m|s|p|f]"
    echo "         -m [profiling|benchmark], specify benchmark mode"
    echo "         -s hostname, host name"
    echo "         -p port, port number"
    echo "         -f output file path"
    echo "         -a other args"
    echo "dirname: test module name"
}

build() {
    mvn --projects benchmark-base,client-base,server-base,${PROJECT_DIR} clean package
}

java_options() {
    JAVA_OPTIONS="-server -Xmx1g -Xms1g -XX:MaxDirectMemorySize=1g -XX:+UseG1GC"
    if [ "x${MODE}" = "xprofiling" ]; then
        # 检查目录是否存在
        profiling_directory="profiling/${PROJECT_DIR}"
        if [ ! -d "${profiling_directory}" ]; then
            echo "creating profiling directory：${profiling_directory}"
            mkdir -p "${profiling_directory}"
        else
            echo "profiling directory exists：${profiling_directory}"
        fi

        if [ "${OS}" = "Darwin" ]; then
            JAVA_OPTIONS="${JAVA_OPTIONS} -agentpath:async-profiler/libasyncProfiler.dylib=start,all-user=true,cstack=no,event=cpu,file=${profiling_directory}/%t.jfr"
            echo "Using: Darwin"
        elif [ "${ARCH}" = "x86_64" ]; then
            JAVA_OPTIONS="${JAVA_OPTIONS} -agentpath:async-profiler/libasyncProfiler_x86_64.so=start,all-user=true,cstack=no,event=cpu,file=${profiling_directory}/%t.jfr"
            echo "Using: x86_64"
        elif [ "${ARCH}" = "aarch64" ]; then
            JAVA_OPTIONS="${JAVA_OPTIONS} -agentpath:async-profiler/libasyncProfiler_aarch64.so=start,all-user=true,cstack=no,event=cpu,file=${profiling_directory}/%t.jfr"
            echo "Using: aarch64"
        else
            echo "Unsupported platform: ${OS} ${ARCH}"
            exit 1
        fi
    fi
}

run() {
    if [ -d "${PROJECT_DIR}/target" ]; then
        JAR=`find ${PROJECT_DIR}/target/*.jar | head -n 1`
        echo
        echo "RUN ${PROJECT_DIR} IN ${MODE:-benchmark} MODE"
        CMD="java ${JAVA_OPTIONS} -Dserver.host=${SERVER} -Dserver.port=${PORT} -Dbenchmark.output=${OUTPUT} -jar ${JAR} ${OTHERARGS} "
        echo "command is: ${CMD}"
        echo
        ${CMD}
    fi
}

PROGRAM_NAME=$0
MODE="benchmark"
SERVER="localhost"
PORT="8080"
OUTPUT=""
OPTIND=1
OTHERARGS=""

while getopts "m:s:p:f:a:" opt; do
    case "$opt" in
        m)
            MODE=${OPTARG}
            ;;
        s)
            SERVER=${OPTARG}
            ;;
        p)
            PORT=${OPTARG}
            ;;
        f)
            OUTPUT=${OPTARG}
            ;;
        a)
            OTHERARGS=${OPTARG}
            ;;
        ?)
            usage
            exit 0
            ;;
    esac
done

shift $((OPTIND-1))
PROJECT_DIR=$1

if [ ! -d "${PROJECT_DIR}" ]; then
    usage
    exit 0
fi

build
java_options
run






