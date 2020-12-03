import java.util.Properties;

public class Config
{
	Properties configFile;

	public Config()
	{
		configFile = new java.util.Properties();
		try {
			configFile.load(this.getClass().getClassLoader().
					getResourceAsStream("config.cfg"));
		}catch(Exception eta){
			eta.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Config val= new Config();
		System.out.println(val.getProperty("mServer"));

	}
	public String getProperty(String key)
	{
		String value = this.configFile.getProperty(key);
		return value;
	}
	public int getIntProperty(String key)
	{
		String value = this.configFile.getProperty(key);
		return Integer.parseInt(value);
	}

}