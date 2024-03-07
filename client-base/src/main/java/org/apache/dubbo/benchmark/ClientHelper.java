/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.benchmark;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

class ClientHelper {

    private ClientHelper() {

    }

    static Options getSupports() {
        Options options = new org.apache.commons.cli.Options();
        options.addOption(Option.builder().longOpt("warmupIterations").hasArg().build());
        options.addOption(Option.builder().longOpt("warmupTime").hasArg().build());
        options.addOption(Option.builder().longOpt("measurementIterations").hasArg().build());
        options.addOption(Option.builder().longOpt("measurementTime").hasArg().build());
        options.addOption(Option.builder().longOpt("resultFormat").hasArg().build());
        return options;
    }

    static CommandLine parseLine(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse(getSupports(), args);
    }

    static Arguments parseArguments(String[] args) throws ParseException {
        CommandLine line = parseLine(args);
        int warmupIterations = Integer.parseInt(line.getOptionValue("warmupIterations", "3"));
        int warmupTime = Integer.parseInt(line.getOptionValue("warmupTime", "10"));
        int measurementIterations = Integer.parseInt(line.getOptionValue("measurementIterations", "3"));
        int measurementTime = Integer.parseInt(line.getOptionValue("measurementTime", "10"));
        String format = line.getOptionValue("resultFormat", "json");
        return new Arguments(warmupIterations, warmupTime, measurementIterations, measurementTime, format);
    }

    static class Arguments {

        public int warmupIterations = 3;

        public int warmupTime = 10;

        public int measurementIterations = 3;

        public int measurementTime = 10;

        public String resultFormat = "json";

        public Arguments() {
        }

        public Arguments(int warmupIterations, int warmupTime, int measurementIterations, int measurementTime, String resultFormat) {
            this.warmupIterations = warmupIterations;
            this.warmupTime = warmupTime;
            this.measurementIterations = measurementIterations;
            this.measurementTime = measurementTime;
            this.resultFormat = resultFormat;
        }

        public int getWarmupIterations() {
            return warmupIterations;
        }

        public void setWarmupIterations(int warmupIterations) {
            this.warmupIterations = warmupIterations;
        }

        public int getWarmupTime() {
            return warmupTime;
        }

        public void setWarmupTime(int warmupTime) {
            this.warmupTime = warmupTime;
        }

        public int getMeasurementIterations() {
            return measurementIterations;
        }

        public void setMeasurementIterations(int measurementIterations) {
            this.measurementIterations = measurementIterations;
        }

        public int getMeasurementTime() {
            return measurementTime;
        }

        public void setMeasurementTime(int measurementTime) {
            this.measurementTime = measurementTime;
        }

        public String getResultFormat() {
            return resultFormat;
        }

        public void setResultFormat(String resultFormat) {
            this.resultFormat = resultFormat;
        }
    }
}
