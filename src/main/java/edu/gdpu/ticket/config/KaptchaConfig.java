package edu.gdpu.ticket.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
	/*@Bean
	public DefaultKaptcha getDefaultKaptcha() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		// 图片边框
		properties.setProperty("kaptcha.border", "yes");
		// 边框颜色
		properties.setProperty("kaptcha.border.color", "105,179,90");
		// 字体颜色
		properties.setProperty("kaptcha.textproducer.font.color", "red");
		// 图片宽
		properties.setProperty("kaptcha.image.width", "110");
		// 图片高
		properties.setProperty("kaptcha.image.height", "40");
		// 字体大小
		properties.setProperty("kaptcha.textproducer.font.size", "35");
		// session key
		properties.setProperty("kaptcha.session.key", "code");
		// 验证码长度
		properties.setProperty("kaptcha.textproducer.char.length", "4");
		// 字体
		properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);

		return defaultKaptcha;
	}*/

	@Value("${kaptcha.border}")
	private String border;
	@Value("${kaptcha.textproducer.font.color}")
	private String fontColor;
	@Value("${kaptcha.image.width}")
	private String imageWidth;
	@Value("${kaptcha.image.height}")
	private String imageHeight;
	@Value("${kaptcha.textproducer.char.string}")
	private String charString;
	@Value("${kaptcha.textproducer.char.length}")
	private String charLength;
	@Value("${kaptcha.textproducer.font.names}")
	private String fontNames;
	@Value("${kaptcha.noise.impl}")
	private String noiseImpl;
	@Value("${kaptcha.obscurificator.impl}")
	private String obscurificatorImpl;
	@Value("${kaptcha.textproducer.font.size}")
	private String fontSize;
	@Value("${kaptcha.session.key}")
	private String sessionKey;
	@Value("${kaptcha.textproducer.impl}")
	private String textProducerImpl;
	@Value("${kaptcha.background.clear.from}")
	private String backgroundClearFrom;
	@Value("${kaptcha.background.clear.to}")
	private String backgroundClearTo;

	@Bean
	public DefaultKaptcha producer() {
		DefaultKaptcha kaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.put("kaptcha.border", border);
		properties.put("kaptcha.textproducer.font.color", fontColor);
		properties.put("kaptcha.image.width", imageWidth);
		properties.put("kaptcha.image.height", imageHeight);
		properties.put("kaptcha.textproducer.char.string", charString);
		properties.put("kaptcha.textproducer.char.length", charLength);
		properties.put("kaptcha.textproducer.font.names", fontNames);
		properties.put("kaptcha.noise.impl", noiseImpl);
		properties.put("kaptcha.obscurificator.impl", obscurificatorImpl);
		properties.put("kaptcha.textproducer.font.size", fontSize);
		properties.put("kaptcha.session.key", sessionKey);
		properties.put("kaptcha.textproducer.impl", textProducerImpl);
		properties.put("kaptcha.background.clear.from", backgroundClearFrom);
		properties.put("kaptcha.background.clear.to", backgroundClearTo);
		Config config = new Config(properties);
		kaptcha.setConfig(config);
		return kaptcha;
	}
}