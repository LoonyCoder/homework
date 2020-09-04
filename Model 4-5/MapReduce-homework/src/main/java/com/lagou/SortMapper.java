package com.lagou;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class SortMapper extends Mapper<Object , Text , IntWritable, NullWritable>{

    IntWritable num=new IntWritable();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        num.set(Integer.valueOf(line));
        context.write(num, NullWritable.get());

    }
}
