package com.appsdeveloperblog.ws.shared;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.appsdeveloperblog.ws.shared.dto.UserDto;
 
public class AmazonSES {
	
	final String FROM = "dale.alexis96@gmail.com";
	final String SUBJECT = "One last step to complete your registration with PhotoApp";
	
	final String PASSWORD_RESET_SUBJECT = "Password reset request";
	
	final String HTMLBODY = "<h1>Please verify your email address.</h1>"
			+ "<p><a href='http://localhost:8080/verification-service/email-verification.html?token=$tokenValue'/></p>";
	
	final String TEXTBODY = "Please verify your email address. "
			+ "http://localhost:8080/verification-service/email-verification.html?token=$tokenValue";

	final String PASSWORD_RESET_HTMLBODY = "<h1>A request to reset your password</h1>"
			+ "Hi, $firstName! "
			+ "<p>Click the link below to reset your password</p>"
			+ "<p><a href='http://localhost:8080/verification-service/password-reset.html?token=$tokenValue'/></p>";
	
	final String PASSWORD_RESET_TEXTBODY = "A request to reset your password"
			+ "Hi, $firstName! "
			+ "Click the link below to reset your password"
			+ "http://localhost:8080/verification-service/password-reset.html?token=$tokenValue";
	
	public boolean sendPasswordResetRequest(String firstName, String email, String token) {
		boolean returnValue = false;
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
		
		String 	htmlBodyWithToken = PASSWORD_RESET_HTMLBODY.replace("$tokenValue", token);
				htmlBodyWithToken = htmlBodyWithToken.replace("$firstName", firstName);
		String 	textBodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
				textBodyWithToken = textBodyWithToken.replace("$firstName", firstName);
				
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(email))
				.withMessage(new Message()
						.withBody(new Body()
									.withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
									.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken))
								)
						.withSubject(new Content().withCharset("UTF-8").withData(PASSWORD_RESET_SUBJECT))
						)
				.withSource(FROM);
		
		client.sendEmail(request);
		
		SendEmailResult result = client.sendEmail(request);
		
		if(result != null && (result.getMessageId() != null && !result.getMessageId().isEmpty())) {
			returnValue = true;
		}
		
		return returnValue;
	}
	
	public void verifyEmail(UserDto userDto) {
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
		
		String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
		String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
		
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(userDto.getEmail()))
				.withMessage(new Message()
						.withBody(new Body()
									.withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
									.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken))
								)
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT))
						)
				.withSource(FROM);
		
		client.sendEmail(request);
		
		System.out.println("Email sent");
	}
}
