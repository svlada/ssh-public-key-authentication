package com.svlada;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.svlada.ssh.PublicKeySshSession;
import com.svlada.ssh.logger.JschLogger;

/**
 * Demonstrate public key authentication and execution of simple command over SSH.
 * 
 * @author vladimir.stankovic (svlada@gmail.com)
 *
 */
public class App {
	
	static final Logger logger = Logger.getLogger(App.class);
	
	public static void main(String[] args) {
		
		Properties config = App.getConfig(args);
		
		final PublicKeySshSession session = new PublicKeySshSession.Builder()
			.host(config.getProperty("host"))
			.username(config.getProperty("username"))
			.privateKeyPath(config.getProperty("privateKeyPath"))
			.port(Integer.parseInt(config.getProperty("port")))
			.logger(new JschLogger())
			.build();
		
		if (session == null) {
			System.exit(-1);
		}
		
		session.execute("echo \"Sit down, relax, mix yourself a drink and enjoy the show...\" >> /tmp/test.out");
		
	}
	
	public static Properties getConfig(String[] args) {
		String profile = "local";
		
		if (args != null && args.length > 0) {
			profile = args[0];
		}
        
		Properties properties = new Properties();
        
		try {
			properties.load(App.class.getClassLoader().getResourceAsStream("config/" + profile + "/ssh.config"));
		} catch (IOException e) {
			logger.error("Failed to load properties.", e);
			return null;
		}
        
		return properties;
	}

}
