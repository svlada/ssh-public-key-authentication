package com.svlada.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * 
 * 
 * @author vladimir.stankovic (svlada@gmail.com)
 *
 */
public class ExecCommand {
	
	public void execute(Session session, String command) {
		
		if (session == null) {
			throw new IllegalArgumentException("Session object is null.");
		}
		
		if (command != null && !command.isEmpty()) {
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
	
}
