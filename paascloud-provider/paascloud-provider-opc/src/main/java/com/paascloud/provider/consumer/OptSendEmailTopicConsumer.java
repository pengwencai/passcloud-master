package com.paascloud.provider.consumer;

import com.paascloud.provider.model.dto.PcSendEmailRequest;
import com.paascloud.provider.service.OptSendMailService;
import com.passcloud.common.core.mq.MqMessage;
import com.passcloud.common.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * The class Opt send email topic consumer.
 *
 * @author liyuzhang
 */
@Slf4j
@Service
public class OptSendEmailTopicConsumer {

	@Resource
	private OptSendMailService optSendMailService;

	/**
	 * Handler send email topic.
	 *
	 * @param body      the body
	 * @param topicName the topic name
	 * @param tags      the tags
	 * @param keys      the keys
	 */
	public void handlerSendEmailTopic(String body, String topicName, String tags, String keys) {
		MqMessage.checkMessage(body, keys, topicName);
		PcSendEmailRequest request;
		try {
			request = JacksonUtil.parseJson(body, PcSendEmailRequest.class);
		} catch (Exception e) {
			log.error("发送短信MQ出现异常={}", e.getMessage(), e);
			throw new IllegalArgumentException("处理MQ信息,JSON转换异常");
		}
		String subject = request.getSubject();
		String text = request.getText();
		Set<String> to = request.getTo();
		optSendMailService.sendTemplateMail(subject, text, to);
	}
}
