package com.test;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class CustomKey implements WritableComparable<CustomKey> {
	private Integer userId;
	private Integer dataSetType;

	public CustomKey() {
	}

	public CustomKey(Integer userId, Integer dataSetType) {
		super();
		this.userId = userId;
		this.dataSetType = dataSetType;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getDataSetType() {
		return dataSetType;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(userId);
		out.writeInt(dataSetType);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		userId = in.readInt();
		dataSetType = in.readInt();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataSetType == null) ? 0 : dataSetType.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomKey other = (CustomKey) obj;
		if (dataSetType == null) {
			if (other.dataSetType != null)
				return false;
		} else if (!dataSetType.equals(other.dataSetType))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public int compareTo(CustomKey o) {
		int returnValue = compare(userId, o.getUserId());
		if (returnValue != 0) {
			return returnValue;
		}
		return compare(dataSetType, o.getDataSetType());
	}

	public static int compare(int k1, int k2) {
		return (k1 < k2 ? -1 : (k1 == k2 ? 0 : 1));
	}

	@Override
	public String toString() {
		return "CustomKey [userId=" + userId + ", dataSetType=" + dataSetType + "]";
	}

}
