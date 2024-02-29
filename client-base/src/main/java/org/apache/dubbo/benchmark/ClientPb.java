package org.apache.dubbo.benchmark;

import com.google.protobuf.util.Timestamps;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.dubbo.benchmark.bean.PagePB;
import org.apache.dubbo.benchmark.bean.UserServiceDubbo;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@State(Scope.Benchmark)
public class ClientPb {
    private static final int CONCURRENCY = 32;

    private final ClassPathXmlApplicationContext context;
    private final UserServiceDubbo.IUserService userService;
    private final AtomicInteger counter = new AtomicInteger(0);

    public ClientPb() {
        context = new ClassPathXmlApplicationContext("consumer.xml");
        context.start();
        userService = (UserServiceDubbo.IUserService) context.getBean("userService");
    }


    @TearDown
    public void close() throws IOException {
        context.close();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean existUser() throws Exception {
        final int count = counter.getAndIncrement();
        return userService.existUser(PagePB.Request.newBuilder().setEmail(String.valueOf(count)).build())
                .getState();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean createUser() throws Exception {
        final int count = counter.getAndIncrement();

        final PagePB.User.Builder user = PagePB.User.newBuilder();
        user.setId(count);
        user.setName(new String("Doug Lea"));
        user.setSex(1);
        user.setBirthday(Timestamps.fromMillis(System.currentTimeMillis()));
        user.setEmail(new String("dong.lea@gmail.com"));
        user.setMobile(new String("18612345678"));
        user.setAddress(new String("北京市 中关村 中关村大街1号 鼎好大厦 1605"));
        user.setIcon(new String("https://www.baidu.com/img/bd_logo1.png"));
        user.setStatus(1);
        user.setCreateTime(Timestamps.fromMillis(System.currentTimeMillis()));
        user.setUpdateTime(user.getCreateTime());
        List<Integer> permissions = new ArrayList<Integer>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 19, 88, 86, 89, 90, 91, 92));
        user.addAllPermissions(permissions);
        final PagePB.Request.Builder builder = PagePB.Request.newBuilder();
        return userService.createUser(builder.setUser(user.build()).build()).getState();

    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public PagePB.User getUser() throws Exception {
        final int count = counter.getAndIncrement();
        return userService.getUser(PagePB.Request.newBuilder().setId(count).build()).getUser();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public PagePB.Page listUser() throws Exception {
        final int count = counter.getAndIncrement();
        return userService.listUser(PagePB.Request.newBuilder().setPage(count).build()).getPage();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(args);
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();

        options.addOption(Option.builder().longOpt("warmupIterations").hasArg().build());
        options.addOption(Option.builder().longOpt("warmupTime").hasArg().build());
        options.addOption(Option.builder().longOpt("measurementIterations").hasArg().build());
        options.addOption(Option.builder().longOpt("measurementTime").hasArg().build());
        options.addOption(Option.builder().longOpt("resultFormat").hasArg().build());

        CommandLineParser parser = new DefaultParser();

        CommandLine line = parser.parse(options, args);

        int warmupIterations = Integer.valueOf(line.getOptionValue("warmupIterations", "3"));
        int warmupTime = Integer.valueOf(line.getOptionValue("warmupTime", "10"));
        int measurementIterations = Integer.valueOf(line.getOptionValue("measurementIterations", "3"));
        int measurementTime = Integer.valueOf(line.getOptionValue("measurementTime", "10"));
        String format = line.getOptionValue("resultFormat", "json");

        Options opt;
        ChainedOptionsBuilder optBuilder = new OptionsBuilder()
                .resultFormat(ResultFormatType.valueOf(format.toUpperCase()))
                .result(System.currentTimeMillis() + "." + format)
                .include(ClientPb.class.getSimpleName())
                .warmupIterations(warmupIterations)
                .warmupTime(TimeValue.seconds(warmupTime))
                .measurementIterations(measurementIterations)
                .measurementTime(TimeValue.seconds(measurementTime))
                .threads(CONCURRENCY)
                .forks(1);

        opt = doOptions(optBuilder).build();

        new Runner(opt).run();
    }

    private static ChainedOptionsBuilder doOptions(ChainedOptionsBuilder optBuilder) {
        String output = System.getProperty("benchmark.output");
        if (output != null && !output.trim().isEmpty()) {
            optBuilder.output(output);
        }
        return optBuilder;
    }
}
