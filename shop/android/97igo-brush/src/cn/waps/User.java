package cn.waps;

public class User
{
	public String imei;
	public String imsi;
	public String mac;
	public String version;
	public String model;
	public String screenWidth;
	public String screenHeight;
	public String brand;

	public void parse(String record)
	{
		String[] it = record.split("\t", Integer.MAX_VALUE);
		this.imei = it[0];
		this.imsi = it[1];
		this.mac = it[2];
		this.version = it[3];
		this.model = it[4];
		this.screenWidth = it[5];
		this.screenHeight = it[6];
		this.brand = it[7];
	}
}
