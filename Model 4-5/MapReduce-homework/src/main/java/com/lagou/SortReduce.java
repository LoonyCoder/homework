package com.lagou;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;


public class SortReduce extends Reducer< IntWritable, NullWritable, Text, NullWritable> {
    private static int countnum = 0;
    private static int temNum = 0;
    Text kout = new Text();
    public void reduce(IntWritable key,Iterable<NullWritable> values,Context context) throws IOException, InterruptedException{
        for(NullWritable niv : values){
            if (key.get() > temNum) {
                countnum++;//全局排序变量
                temNum = key.get();//记录当前临时值
            }
            String kk = countnum + "\t" + key.toString();
            kout.set(kk);
            context.write(kout, NullWritable.get());
        }


    }

}
