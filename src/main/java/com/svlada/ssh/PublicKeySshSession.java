package com.svlada.ssh;

import org.apache.commons.lang3.Validate;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.svlada.ssh.logger.JschLogger;

/**
 * Helper class for building {@link Session} objects.
 * 
 * @author vladimir.stankovic (svlada@gmail.com)
 *
 */
public class PublicKeySshSession {
	
	final Session session;
	
	public PublicKeySshSession(final Session session) {
		this.session = session;
	}

	public void execute(String command) {
		
		if (session == null) {
			throw new IllegalArgumentException("Session object is null.");
		}
		
		if (command != null && command.isEmpty()) {
			throw new IllegalArgumentException("SSH command is blank.");
		}
		
		try {
			
			session.connect();
			
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			((ChannelExec) channel).setPty(false);
			
			channel.connect();
			
			channel.disconnect();
			session.disconnect();
			
		} catch (JSchException e) {
			throw new RuntimeException("Error durring SSH command execution. Command: " + command);
		}
	}
	
	public static class Builder {
		private String host;
		private String username;
		private int port;
		private String privateKeyPath;
		private com.jcraft.jsch.Logger logger;

		private void validate() {
			Validate.notBlank(host);
			Validate.notBlank(username);
			Validate.notBlank(privateKeyPath);
			if (port < 1) {
				throw new IllegalArgumentException("Port number must start with 1.");
			}
		}

		public PublicKeySshSession build() {
			validate();

			if (logger != null) {
				JSch.setLogger(new JschLogger());
			}

			JSch jsch = new JSch();

			Session session = null;

			try {

				jsch.addIdentity(privateKeyPath);

				session = jsch.getSession(username, host, port);
				session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");

				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");

				session.setConfig(config);

			} catch (JSchException e) {
				throw new RuntimeException("Failed to create Jsch Session object.", e);
			}

			return new PublicKeySshSession(session);
		}

		public Builder logger(com.jcraft.jsch.Logger logger) {
			this.logger = logger;
			return this;
		}

		public Builder host(String host) {
			this.host = host;
			return this;
		}

		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder port(int port) {
			this.port = port;
			return this;
		}

		public Builder privateKeyPath(String privateKeyPath) {
			this.privateKeyPath = privateKeyPath;
			return this;
		}

	}

}
