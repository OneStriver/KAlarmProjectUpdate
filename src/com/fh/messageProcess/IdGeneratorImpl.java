/**
 * 
 */
package com.fh.messageProcess;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author xunzy
 * 
 */
public class IdGeneratorImpl implements IdGenerator {

	public String genId() {
		return RandomStringUtils.randomAlphanumeric(8);
	}
}
