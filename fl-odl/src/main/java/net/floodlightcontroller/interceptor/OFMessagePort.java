/**
 * Copyright (c) 2014, NetIDE Consortium (Create-Net (CN), Telefonica Investigacion Y Desarrollo SA (TID), Fujitsu 
 * Technology Solutions GmbH (FTS), Thales Communications & Security SAS (THALES), Fundacion Imdea Networks (IMDEA),
 * Universitaet Paderborn (UPB), Intel Research & Innovation Ireland Ltd (IRIIL) )
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Authors:
 *     aleckey
 */
package net.floodlightcontroller.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.openflow.protocol.OFPhysicalPort;

/**
 * Parses an OF Port message into its relevant properties
 *
 */
public class OFMessagePort {
	private String action;
	private long switchId;
	private short portNo;
	private List<String> portFeatures = new ArrayList<String>();
	private OFPhysicalPort ofPort;

	/**@return the action */
	public String getAction() {
		return action;
	}

	/**@return the switch id */
	public long getSwitchId() {
		return switchId;
	}

	/**@return the portNo */
	public short getPortNo() {
		return portNo;
	}
	
	/**@return the portFeatures */
	public List<String> getPortFeatures() {
		return portFeatures;
	}
	
	/**@return the ofPort */
	public OFPhysicalPort getOfPort() {
		return ofPort;
	}
	
	public OFMessagePort() { }
	
	/**
	 * Parses message into relevant properties
	 * @param rawMessage Assumes the following format: ["port", "join", 1, 3, true, true, ["OFPPF_COPPER", "OFPPF_10GB_FD"]] and parses into properties
	 * @return
	 */
	public OFMessagePort(String rawMessage) {
		parseMessage(rawMessage);
	}
	
	/**
	 * Parses message into relevant properties
	 * @param rawMessage Assumes the following format: ["port", "join", 1, 3, true, true, ["OFPPF_COPPER", "OFPPF_10GB_FD"]] and parses into properties
	 * @return
	 */
	public void parseMessage(String rawMessage) {
		String tmp = rawMessage.substring(1, rawMessage.length()-1); //STRIP [ ]
		String[] props = tmp.split(","); 

		this.action = props[1].trim().substring(1, props[1].trim().length()-1); //STRIP " "
		this.switchId = Long.parseLong(props[2].trim());
		this.portNo = Short.parseShort(props[3].trim());
		//PORT PROPERTIES
		if (props.length > 6) { 
			for (int i=6; i<props.length; i++) {
				String portProp = props[i].trim().substring(1, props[i].trim().length()-1);
				if (portProp.startsWith("\"")) portProp = portProp.substring(1); //STRIP STARTING [
				if (portProp.endsWith("\"")) portProp = portProp.substring(0, portProp.length()-1); //STRIP ENDING ]
				this.portFeatures.add(portProp);
			}
		}
		if (this.action.equals("join"))
			createPhysicalPort(rawMessage);
	}

	private void createPhysicalPort(String rawMessage) {
		ofPort = new OFPhysicalPort();
		ofPort.setPortNumber(this.portNo);
		ofPort.setName(String.valueOf(this.portNo));
		int totalFeatures = 0;
		for (String feature: this.portFeatures) {
			totalFeatures += OFPhysicalPort.OFPortFeatures.valueOf(feature).getValue();		
		}
		ofPort.setSupportedFeatures(totalFeatures);
		ofPort.setAdvertisedFeatures(totalFeatures);
	}

}
