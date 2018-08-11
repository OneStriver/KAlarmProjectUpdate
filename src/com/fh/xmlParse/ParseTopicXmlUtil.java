package com.fh.xmlParse;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 解析主题消息
 */
public class ParseTopicXmlUtil {

	public static List<String> toopicList = new ArrayList<String>();

	private static ParseTopicXmlUtil parseTopicXmlUtil = new ParseTopicXmlUtil();

	private ParseTopicXmlUtil() {

	}

	public static synchronized ParseTopicXmlUtil getInstance() {
		return parseTopicXmlUtil;
	}

	public List<String> parseTopicXml(String fileName) {
		toopicList.clear();
		SAXReader reader = new SAXReader();
		Document document;
		try {
			String topicXmlPath = ParseTopicXmlUtil.class.getResource(fileName).getPath();
			document = reader.read(new File(topicXmlPath));
			Element alarm = document.getRootElement();
			Iterator<?> it = alarm.elementIterator();
			while (it.hasNext()) {
				Element topicTypeAndAlm = (Element) it.next();
				if ("topicType".equals(topicTypeAndAlm.getName())) {
					toopicList.add(topicTypeAndAlm.getTextTrim());
					System.out.println(" = = >> Receive Topic : " + topicTypeAndAlm.getTextTrim());
				}
			}
			return toopicList;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

}
