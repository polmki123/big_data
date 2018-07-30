package com.test;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.javamakeuse.bd.poc.mapper.UserActivityMapper;
import com.javamakeuse.bd.poc.mapper.UserMapper;
import com.javamakeuse.bd.poc.reducer.UserActivityReducer;
import com.javamakeuse.bd.poc.util.CustomKey;
import com.javamakeuse.bd.poc.util.GroupComparator;
import com.javamakeuse.bd.poc.util.KeyPartitioner;

public class UserActivityDriver extends Configured implements Tool {

	public static void main(String[] args) {
		try {
			int status = ToolRunner.run(new UserActivityDriver(), args);
			System.exit(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int run(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.printf("Usage: %s [generic options] <input1> <output>\n", getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		Job job = Job.getInstance();
		job.setJarByClass(getClass());
		job.setJobName("ReduceSideJoin Example");
		// input paths
		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, UserActivityMapper.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, UserMapper.class);

		// output path
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		job.setReducerClass(UserActivityReducer.class);

		job.setPartitionerClass(KeyPartitioner.class);
		job.setGroupingComparatorClass(GroupComparator.class);

		job.setMapOutputKeyClass(CustomKey.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

}
