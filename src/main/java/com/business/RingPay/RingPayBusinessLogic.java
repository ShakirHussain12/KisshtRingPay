package com.business.RingPay;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.android.RingPayPages.AddAddressPage;
import com.android.RingPayPages.FeeDetailsPage;
import com.android.RingPayPages.GmailLoginPage;
import com.android.RingPayPages.HomePage;
import com.android.RingPayPages.MerchantOfferPage;
import com.android.RingPayPages.MobileLoginPage;
import com.android.RingPayPages.PromoCodeOfferPage;
import com.android.RingPayPages.RingLoginPage;
import com.android.RingPayPages.RingPayMerchantFlowPage;
import com.android.RingPayPages.RingPromoCodeLogin;
import com.android.RingPayPages.RingUserDetailPage;
import com.android.RingPayPages.SignUP_LoginPage;
import com.android.RingPayPages.TermsAndConditionPage;
import com.android.RingPayPages.UserRegistrationPage;
import com.driverInstance.CommandBase;
import com.extent.ExtentReporter;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class RingPayBusinessLogic extends Utilities {

	public RingPayBusinessLogic(String Application) throws InterruptedException {
		new CommandBase(Application);
		init();
	}

	
	private int timeout;
	public long age;
	SoftAssert softAssertion = new SoftAssert();
	boolean launch = "" != null;
	/** Retry Count */
	private int retryCount;
	ExtentReporter extent = new ExtentReporter();

	/** The Constant logger. */
	static LoggingUtils logger = new LoggingUtils();

	/** The Android driver. */
	public AndroidDriver<AndroidElement> androidDriver;
	public String amount[] = { "1001", "0", "1" };
	public static boolean relaunchFlag = false;
	public static boolean appliTools = false;
	public String noTxt;
	public static boolean PopUp = false;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;

	}

	/**
	 * Initiate Property File.
	 *
	 * @param byLocator the by locator
	 */

	public void init() {

		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
		setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
		logger.info(
				"Loaded the following properties" + " TimeOut :" + getTimeout() + " RetryCount :" + getRetryCount());
	}

	public void TearDown() {
		logger.info("App tear Down");
		getDriver().quit();
	}

	/**
	 * Business method for UserPlayStore Flow
	 * 
	 */

	public void User_Play_Store_Flow(String validMob, String editMob, String lessThanTenMob, String moreThanTenMob,
			String specialCharMob, String spaceMob, String lessOtp, String invalidOtp) throws Exception {
		extent.HeaderChildNode("User Play store Flow Module");

		cameraPermission();
		extent.extentLogger("PASS",
				"TC_Ring_Core_01 - To Verify the Login screen when user opens the app by clicking on App Icon");

		enablePermissions();
		extent.extentLoggerPass("TC_Ring_Core_02",
				"TC_Ring_Core_02 - To verify When User selects Enable Permission option");

		promoCodeModule();
		extent.extentLoggerPass("TC_Ring_Core_66", "TC_Ring_Core_66 - To verify  user Scans the Promo Code QR");

		String loginHeaderTxt = getText(RingLoginPage.objLoginHeader);
		softAssertion.assertEquals(loginHeaderTxt, "Sign Up / Login");

		String mobileTxt = getText(RingLoginPage.objLoginMobile);
		String googleTxt = getText(RingLoginPage.objLoginGoogle);
		String facebookTxt = getText(RingLoginPage.objLoginFacebook);
		String termsTxt = getText(RingLoginPage.objTermsLink_PrivacyFooter);

		softAssertion.assertEquals(mobileTxt, "Continue with Mobile");
		softAssertion.assertEquals(googleTxt, "Continue with Google");
		softAssertion.assertEquals(facebookTxt, "Continue with Facebook");
		softAssertion.assertEquals(termsTxt, " Terms of Services &  Privacy Policy");

		extent.extentLoggerPass("TC_Ring_Core_03",
				"TC_Ring_Core_03 - To verify User Selects signup/Login option under Don't have a QR Code?");

		loginMobile();
		extent.extentLoggerPass("TC_Ring_Core_04", "TC_Ring_Core_04 - To Verify when user Continue with mobile option");

		extent.extentLoggerPass("TC_Ring_Core_05", "TC_Ring_Core_05 - To Verify the Verify mobile screen");

		logger.info("Verify mobile number with <10 digits");
		mobileNoValidation1(lessThanTenMob);
		explicitWaitVisibility(RingLoginPage.objMobError, 10);
		String errorMsg = getText(RingLoginPage.objMobError);
		softAssertion.assertEquals(errorMsg, " Please enter valid mobile number");
		extent.extentLoggerPass("TC_Ring_Core_07",
				"TC_Ring_Core_07 - To Verify User enter mobile number less than 10 digit");

		clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
		logger.info("Verify mobile number with >10 digits");
		mobileNoValidation1(moreThanTenMob);
		String otpAutoRead = getText(RingLoginPage.OtpAutoRead);
		softAssertion.assertEquals(otpAutoRead, "Auto Reading OTP");
		extent.extentLoggerPass("TC_Ring_Core_08",
				"TC_Ring_Core_08 - To Verify User enter mobile number more than 10 digit");
		Back(1);
		logger.info("Verify mobile number with special characters");
		mobileNoValidation(specialCharMob);
		explicitWaitVisibility(RingLoginPage.objMobError, 10);
		softAssertion.assertEquals(errorMsg, " Please enter valid mobile number");
		extent.extentLoggerPass("TC_Ring_Core_10",
				"TC_Ring_Core_10 - To Verify User tries enter punctuations or special character in field");

		clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
		logger.info("Verify mobile number with space in between");
		mobileNoValidation1(spaceMob);
		explicitWaitVisibility(RingLoginPage.objMobError, 10);
		softAssertion.assertEquals(errorMsg, " Please enter valid mobile number");
		extent.extentLoggerPass("TC_Ring_Core_11",
				"TC_Ring_Core_11 - To Verify User tries enter punctuations or special character in field");

		clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
		
		logger.info("Verify mobile number with entering valid number");
		mobileNoValidation1(validMob);
		explicitWaitVisibility(RingLoginPage.OtpAutoRead, 10);
		
		softAssertion.assertEquals(otpAutoRead, "Auto Reading OTP");
		extent.extentLoggerPass("TC_Ring_Core_13", "TC_Ring_Core_13 - To Verify User tries enter valid mobile number");

		explicitWaitClickable(RingLoginPage.objEditMobNo, 10);

		Aclick(RingLoginPage.objEditMobNo, "Edit Mobile number");
		extent.extentLoggerPass("TC_Ring_Core_14",
				"TC_Ring_Core_14 - To Verify User should able to see Edit mobile no option");

		trueCallerPopup();
		mobileNoValidation1(editMob);
		explicitWaitVisibility(RingLoginPage.getEditMob(editMob), 10);
		String mobNoTxt = getText(RingLoginPage.getEditMob(editMob));
		String mobNoText = mobNoTxt.substring(16, 26);
		System.out.println(mobNoText);
		softAssertion.assertNotEquals(validMob, mobNoText);
		extent.extentLoggerPass("TC_Ring_Core_16",
				"TC_Ring_Core_16 - To verify user clicks continue button after mobile number modification");
		// 18 TO DO
		explicitWaitVisibility(RingLoginPage.OtpAutoRead, 10);
		WebElement resendOtp = findElement(RingLoginPage.resendOtpTxt);
		String clickable = getAttributValue("clickable", RingLoginPage.resendOtpTxt);
		softAssertion.assertEquals("false", clickable);
		extent.extentLoggerPass("TC_Ring_Core_19",
				"TC_Ring_Core_19 - To Verify the text given below the OTP number box when the timer is in progress");

		explicitWaitClickable(RingLoginPage.resendOtpTxt, 10);
		extent.extentLoggerPass("TC_Ring_Core_20",
				"TC_Ring_Core_20 - To Verify the text given below the OTP number box when the timer is completed.");

		String focused_before = getAttributValue("focused", RingLoginPage.objOtpTxtField1);
		System.out.println(focused_before);
		softAssertion.assertEquals("false", focused_before);
		extent.extentLoggerPass("TC_Ring_Core_21",
				"TC_Ring_Core_21 - To Verify the OTP number box behaviour when the timer is started.");

		explicitWaitVisibility(RingLoginPage.resendOtpTxt, 10);
		String focused_after = getAttributValue("clickable", RingLoginPage.objOtpTxtField1);
		System.out.println(focused_after);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		softAssertion.assertEquals("true", focused_after);
		extent.extentLoggerPass("TC_Ring_Core_22",
				"TC_Ring_Core_22 - To Verify the OTP number box behaviour when the timer is completed.");

		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, invalidOtp, "Enter OTP");
		explicitWaitVisibility(RingLoginPage.OtpError, 10);
		logger.info("OTP Error message");
		String otpErrorTxt = getText(RingLoginPage.OtpError);
		softAssertion.assertEquals(otpErrorTxt, "You have entered incorrect or expired OTP");
		extent.extentLoggerPass("TC_Ring_Core_23", "TC_Ring_Core_23 - To Verify User enter invalid OTP");

		clearField(RingLoginPage.objOtpTxtField1, "Enter OTP");
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, lessOtp, "Enter OTP");
		boolean Otp_flag = verifyElementNotPresent(RingLoginPage.OtpError, 10);
		softAssertion.assertEquals(false, Otp_flag);
		extent.extentLoggerPass("TC_Ring_Core_26",
				"TC_Ring_Core_26 - To Verify if user enters less than 6 digit number");

		explicitWaitClickable(RingLoginPage.resendOtpTxt, 10);
		extent.extentLoggerPass("TC_Ring_Core_27", "TC_Ring_Core_27 - To Verify Resend OTP should clickable");
		clearField(RingLoginPage.objOtpTxtField1, "Enter OTP");

		waitTime(3000);
		Back(1);
		Back(1);

		String blockMobileNo = "9" + RandomIntegerGenerator(9);
		mobileNoValidation(blockMobileNo);

		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 1 Time");
		String getAutoreadValidation = getText(MobileLoginPage.txtAutoReadingOTP);
		softAssertion.assertEquals("Auto Reading OTP", getAutoreadValidation);
		logger.info("Auto Reading OTP Validate " + getAutoreadValidation);
		extent.extentLoggerPass("Auto Reading OTP", "Auto Reading OTP Validate Successfully");
		explicitWaitVisibility(MobileLoginPage.txtOtpTimeStamp, 20);
		String otpTimeStamp = getText(MobileLoginPage.txtOtpTimeStamp);
		otpTimeStamp = otpTimeStamp.substring(0, 5);
		System.out.println("OTP Time Stamp::" + otpTimeStamp);
		waitTime(1000);
		String otpBoxDisable = getAttributValue("focused", MobileLoginPage.enterOTPNumberFiled);
		System.out.println("Element Focus:: " + otpBoxDisable);
		softAssertion.assertEquals(" 00:0", otpTimeStamp);
		if (otpBoxDisable.equals("false")) {
			softAssertion.assertEquals("false", otpBoxDisable);
			logger.info("OTP Box Disable");
			extent.extentLoggerPass("OTP Box Disable", "OTP Box Disable");
		} else {
			logger.info("OTP Box Enabled");
			extent.extentLoggerFail("OTP Box Enable", "OTP Box Enabled");
		}

		extent.extentLoggerPass("TC_Ring_Core_28", "TC_Ring_Core_28 - To Verify Resend OTP option");

		// Attemp 2
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, generateRandomInt(1000000), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 2 Time");
		String getAutoreadValidation1 = getText(MobileLoginPage.txtAutoReadingOTP);
		softAssertion.assertEquals("Auto Reading OTP", getAutoreadValidation1);
		logger.info("Auto Reading OTP Validate " + getAutoreadValidation1);
		extent.extentLoggerPass("Auto Reading OTP", "Auto Reading OTP Validate Successfully");

		// Attemp 3
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, generateRandomInt(1000000), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 3 Time");

		// Attemp 4
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);

		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, generateRandomInt(1000000), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 4 Time");

		// Attemp 5
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);

		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, generateRandomInt(1000000), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 5 Time");
		if (verifyElementDisplayed(MobileLoginPage.txtResendOtp)) {
			Aclick(MobileLoginPage.txtResendOtp, "Resend Button 5 Time");
		}

		explicitWaitVisibility(RingLoginPage.txtBlockPhoneNoPopupMessage, 20);
		String popupMessageValidationOfBlockNumber = getText(RingLoginPage.txtBlockPhoneNoPopupMessage);
		logger.info("Expected: " + popupMessageValidationOfBlockNumber);
		softAssertion.assertEquals(
				"Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.",
				popupMessageValidationOfBlockNumber);
		logger.info("Temprory Block Your Number For 2 Minutes Validated PopUp Message");
		extent.extentLoggerPass("PopUp Of Block User For 2 Minutes",
				"Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.");
		Aclick(RingLoginPage.btnOkGotIt, "Button Ok, Got It!");
		trueCallerPopup();
		extent.extentLoggerPass("TC_Ring_Core_29",
				"TC_Ring_Core_29 To Verify user enters wrong otp and attempts for 5th time  by clicking on Resend button ");
		waitTime(4000);
		// TC30
		explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 20);
		String verifyMoblieNumberEnter = getText(RingLoginPage.objVerifyMobHeader);
		softAssertion.assertEquals("Verify Mobile", verifyMoblieNumberEnter);
		logger.info(verifyMoblieNumberEnter);
		extent.extentLoggerPass("Verify Mobile Page", "Verify Mobile Page is visible");
		// logger.info("TC 30----------------->PASSED");
		extent.extentLoggerPass("TC_Ring_Core_30",
				"TC_Ring_Core_30 To Verify when user click on Ok, Got It! on the bottom sheet ");
		// TC31
		waitTime(4000);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, blockMobileNo, "Mobile text field");
		System.out.println(blockMobileNo);
		waitTime(5000);
		Aclick(RingLoginPage.objOkGotitBtn, "Button Ok, Got It!");
		click(RingLoginPage.objMobTextField, "Mobile Number Field");
		clearField(RingLoginPage.objMobTextField, "Mobile Number Field");

		logger.info("Expected: " + popupMessageValidationOfBlockNumber);
		softAssertion.assertEquals(
				"Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.",
				popupMessageValidationOfBlockNumber);
		logger.info("Temprory Block Your Number For 2 Minutes Validated PopUp Message");
		extent.extentLoggerPass("PopUp Of Block User For 2 Minutes",
				"Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.");		logger.info("TC_Ring_Core_31 To Verify when user enter the same mobile number which is blocked------>PASSED");
		extent.extentLoggerPass("TC_Ring_Core_31",
				"TC_Ring_Core_31 To Verify when user enter the same mobile number which is blocked------>PASSED");

		waitTime(4000);
		explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
		logger.info("Verify Mobile Header");
		String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
		softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
		waitTime(4000);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, "9" + RandomIntegerGenerator(9), "Mobile text field");
		enterOtp("888888");
		// TC32
		explicitWaitVisibility(MobileLoginPage.btnReadAndAccept, 10);
		String txtReadAndAccept = getText(MobileLoginPage.btnReadAndAccept);
		logger.info(txtReadAndAccept);
		softAssertion.assertEquals("Read & Accept", txtReadAndAccept);
		extent.extentLoggerPass("Permission Validatation", "Permission Page is visible");
		logger.info("TC_Ring_Core_32 To Verify user enters valid OTP---->PASSED");
		extent.extentLoggerPass("TC_Ring_Core_32", "TC_Ring_Core_32 To Verify user enters valid OTP---->PASSED");

		explicitWaitVisibility(RingLoginPage.objRingPermissionsHeader, 10);

		waitTime(7000);
		Swipe("up", 1);
		explicitWaitVisibility(RingLoginPage.ckycCheckBox, 10);
		Aclick(RingLoginPage.ckycCheckBox, "Terms and Conditions checkbox");

		Aclick(RingLoginPage.objReadAcceptBtn, "Read & Accept button");
		waitTime(1000);
		String kycError = getText(UserRegistrationPage.objToast);
		softAssertion.assertEquals("Please accept terms and conditions", kycError);
		extent.extentLoggerPass("TC_Ring_Core_47",
				"TC_Ring_Core_47 To Verify if user only checked in whatsapp notification check box and Continue with \n"
						+ "read & accept");
		extent.extentLoggerPass("TC_Ring_Core_49",
				"TC_Ring_Core_49 To Verify when user clicks on read and accept with unchecked CKYC consent");

		Aclick(RingLoginPage.ckycCheckBox, "Terms and Conditions checkbox");

		if (verifyElementPresent(RingLoginPage.objRingPermissionsHeader, "RingPay permissions")) {

			logger.info("Ring Pay Permissions page (SMS, LOCATION & PHONE)");
			String ringPermissionTxt = getText(RingLoginPage.objRingPermissionsHeader);
			Assert.assertEquals(ringPermissionTxt, "Permissions");

			Aclick(RingLoginPage.objReadAcceptBtn, "Read & Accept button");

			explicitWaitVisibility(RingLoginPage.objLocAccess, 10);

			Aclick(RingLoginPage.objLocAccess, "Location Access option");
			Aclick(RingLoginPage.objPhoneAccess, "Phone access option");
			Aclick(RingLoginPage.objSMSAccess, "SMS access option");
		}
		extent.extentLoggerPass("TC_Ring_Core_44",
				"TC_Ring_Core_44 To Verify after successful login, user should navigate to permission page");
		extent.extentLoggerPass("TC_Ring_Core_54", "TC_Ring_Core_54 To verify when users allow all the permission");
		softAssertion.assertAll();

	}

	/**
	 * Business method for User Registration Flow
	 * 
	 */
	public void User_Registration_Flow(String month, String date, String year, String gender) throws Exception {
		extent.HeaderChildNode("User Registration Flow Module");

		logger.info("User Registration Details Page");
		extent.extentLogger("INFO", "User Registration Details Page");
		explicitWaitVisibility(UserRegistrationPage.objUserDetailsHeader, 10);
		hideKeyboard();
		explicitWaitVisibility(UserRegistrationPage.objFirstName, 10);
		explicitWaitVisibility(UserRegistrationPage.objLastName, 10);
		explicitWaitVisibility(UserRegistrationPage.objUserDOB, 10);
		explicitWaitVisibility(UserRegistrationPage.objGenderSelect, 10);

		extent.extentLoggerPass("TC_Ring_Core_68", "TC_Ring_Core_68 - To verify the 'User Details' screen");

		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		String firstNameErrorTxt = getText(UserRegistrationPage.objFirstNameError);
		String lastNameErrorTxt = getText(UserRegistrationPage.objLastNameError);
		String dobErrorTxt = getText(UserRegistrationPage.objDOBError);
		String genderErrorTxt = getText(UserRegistrationPage.objGender);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		softAssertion.assertEquals("Please select Date of Birth", dobErrorTxt);
		softAssertion.assertEquals("Please Select Gender.", genderErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_71",
				"TC_Ring_Core_71 - To verify the response by clicking on 'Register' button when "
						+ "all required fields are empty");
		extent.extentLoggerPass("TC_Ring_Core_94",
				"TC_Ring_Core_94 - To verify whether the user is not able to 'Continue' when the 'Gender' is not selected "
						+ "from the drop down");

		Aclick(UserRegistrationPage.objLastName, "Last name text field");
		type(UserRegistrationPage.objLastName, "Huss", "Last Name text field");
		hideKeyboard();
		dateOfBirth(month, date, year);
		Swipe("up", 2);
		click(UserRegistrationPage.objGenderSelect, "Gender dropdown");
		explicitWaitVisibility(UserRegistrationPage.objMale, 10);
		click(UserRegistrationPage.objMale, "Male gender");
		click(UserRegistrationPage.objMale, "Male gender");
		waitTime(3000);
		Swipe("down", 2);
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_72",
				"TC_Ring_Core_72 - To verify the user is not able to 'Continue' with by keeping 'First Name' field "
						+ "empty with all valid details");

		Aclick(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, generateRandomString(1), "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationPage.objRegister, 10);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_73",
				"TC_Ring_Core_73 - To verify the user is not able to 'Continue' with entering only 'First Name' "
						+ "initial in first name field with all valid details");

		clearField(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, "Xyz123", "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationPage.objRegister, 10);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_74",
				"TC_Ring_Core_74 - To verify the user is not able to 'Continue' with alphanumeric characters"
						+ "entered in 'First Name' field with all valid details");

		clearField(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, "Xyz123:+$", "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationPage.objRegister, 10);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_75",
				"TC_Ring_Core_75 - To verify the user is not able to 'Continue' with special characters "
						+ "entered in 'First Name' field with all valid details");

		clearField(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, "  ", "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationPage.objRegister, 10);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_76",
				"TC_Ring_Core_76 - To verify the user is not able to 'Continue' with <Space> entered in 'First "
						+ " Name' field with all valid details");

		clearField(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, generateRandomString(5), "First Name text field");
		hideKeyboard();

		Aclick(UserRegistrationPage.objLastName, "Last Name text field");
		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_79",
				"TC_Ring_Core_79 - To verify the user is not able to 'Continue' with by keeping 'Last Name' field "
						+ "empty with all valid details");

		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		type(UserRegistrationPage.objLastName, generateRandomString(1), "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_80",
				"TC_Ring_Core_80 - To verify the user is not able to 'Continue' with entering only 'Last Name' "
						+ "initial in Last name field with all valid details");

		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		type(UserRegistrationPage.objLastName, "pqr123", "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_81",
				"TC_Ring_Core_81 - To verify the user is not able to 'Continue' with alphanumeric characters "
						+ "entered in 'Last Name' field with all valid details");

		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		type(UserRegistrationPage.objLastName, "pqr123+=", "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_82",
				"TC_Ring_Core_82 - To verify the user is not able to 'Continue' with special characters "
						+ "entered in 'Last Name' field with all valid details");

		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		type(UserRegistrationPage.objLastName, "  ", "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_83",
				"TC_Ring_Core_83 - To verify the user is not able to 'Continue' with <Space> entered in 'Last "
						+ " Name' field with all valid details");

		clearField(UserRegistrationPage.objFirstName, "First Name text field");
		Aclick(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, "Shak", "First Name text field");
		Aclick(UserRegistrationPage.objLastName, "Last name text field");
		type(UserRegistrationPage.objLastName, "Shak", "Last name text field");
		explicitWaitVisibility(RingUserDetailPage.objMothersName, 10);
		click(RingUserDetailPage.objMothersName, "Mothe's Name Field");
		type(RingUserDetailPage.objMothersName, "Mom", "Mother's Name field");
		hideKeyboard();
		waitTime(3000);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		Aclick(UserRegistrationPage.objRegister, "Register Button");

		String toast_85 = getText(UserRegistrationPage.objToast);
		System.out.println(toast_85);
		softAssertion.assertEquals("First and Last name should be different", toast_85);
		extent.extentLoggerPass("TC_Ring_Core_85",
				"TC_Ring_Core_85 - To verify the user is not able to 'Continue' with entering same 'First Name' "
						+ "and  'Last Name'");

		waitTime(3000);
		clearField(UserRegistrationPage.objFirstName, "First Name text field");
		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		Aclick(UserRegistrationPage.objFirstName, "First name text field");
		type(UserRegistrationPage.objFirstName, "Shak Shak", "First Name text field");
		waitTime(3000);
		Aclick(UserRegistrationPage.objLastName, "Last name text field");
		type(UserRegistrationPage.objLastName, "huss", "First Name text field");

		explicitWaitVisibility(RingUserDetailPage.objMothersName, 10);
		click(RingUserDetailPage.objMothersName, "Mothe's Name Field");
		type(RingUserDetailPage.objMothersName, "Mom", "Mother's Name field");

		waitTime(3000);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		waitTime(3000);
		String toast_86 = getText(UserRegistrationPage.objToast);
		System.out.println(toast_86);
		softAssertion.assertEquals("Enter valid First Name ", toast_86);
		extent.extentLoggerPass("TC_Ring_Core_86",
				"TC_Ring_Core_86 - To verify the user is not able to 'Continue' with entering same 'First Name' repeatedly "
						+ "in same field");

		clearField(UserRegistrationPage.objFirstName, "First Name text field");
		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		Aclick(UserRegistrationPage.objFirstName, "Last name text field");
		type(UserRegistrationPage.objFirstName, "shak", "First Name text field");
		Aclick(UserRegistrationPage.objLastName, "Last name text field");
		type(UserRegistrationPage.objLastName, "huss huss", "First Name text field");
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		waitTime(3000);
		String toast_87 = getText(UserRegistrationPage.objToast);
		System.out.println(toast_87);
		softAssertion.assertEquals("Enter valid Last Name ", toast_87);
		extent.extentLoggerPass("TC_Ring_Core_87",
				"TC_Ring_Core_87 - To verify the user is not able to 'Continue' with entering same 'Last Name' repeatedly "
						+ "in same field");

		clearField(UserRegistrationPage.objLastName, "Last Name text field");
		Aclick(UserRegistrationPage.objLastName, "Last name text field");
		type(UserRegistrationPage.objLastName, "huss", "First Name text field");
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		waitTime(3000);
		String toast_88 = getText(UserRegistrationPage.objToast);
		System.out.println(toast_88);
		softAssertion.assertEquals("The email field is required.", toast_88);
		extent.extentLoggerPass("TC_Ring_Core_88",
				"TC_Ring_Core_88 - To verify the user is not able to 'Continue' with by keeping 'Email Address' field "
						+ "empty with all valid details");

		Aclick(UserRegistrationPage.objUserEmail, "Email text field");
		explicitWaitVisibility(UserRegistrationPage.objNoneOfAbove, 10);
		Aclick(UserRegistrationPage.objNoneOfAbove, "None of the above button");
		type(UserRegistrationPage.objUserEmail, "huss^^@gmail.com", "Email text field");
		waitTime(3000);
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objEmailError, 10);
		String emailErrorTxt = getText(UserRegistrationPage.objEmailError);
		softAssertion.assertEquals("Please enter valid email id", emailErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_89",
				"TC_Ring_Core_89 - To verify the user is not able to 'Continue' with by entering 'Email Address' in "
						+ "invalid format OR with special characters with all valid details");

		clearField(UserRegistrationPage.objUserEmail, "Email text field");
		type(UserRegistrationPage.objUserEmail, "huss  @gmail.com", "Email text field");
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objEmailError, 10);
		softAssertion.assertEquals("Please enter valid email id", emailErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_90",
				"TC_Ring_Core_90 - To verify the user is not able to 'Continue' with <Space> entered in 'Email Address' "
						+ " field with all valid details");

		hideKeyboard();
		clearField(UserRegistrationPage.objUserEmail, "Email text field");
		setWifiConnectionToONOFF("Off");
		Aclick(UserRegistrationPage.objRegister, "Register Button");
		explicitWaitVisibility(UserRegistrationPage.objInternetError, 10);
		String intError = getText(UserRegistrationPage.objInternetError);
		softAssertion.assertEquals(" Check your connection & try again ", intError);
		extent.extentLoggerPass("TC_Ring_Core_96",
				"TC_Ring_Core_96 - To verify the user is getting 'Check internet connection' screen after clicking on "
						+ "'Continue' button when the Device internet connection is down");

		setWifiConnectionToONOFF("On");
		explicitWaitClickable(UserRegistrationPage.objGotItBtn, 10);
		Aclick(UserRegistrationPage.objGotItBtn, "Okay Got It button");
		explicitWaitVisibility(UserRegistrationPage.objUserDetailsHeader, 10);
		extent.extentLoggerPass("TC_Ring_Core_97",
				"TC_Ring_Core_97 - To verify the user is able to 'Continue' after 'Okay Got It' once the device internet "
						+ " connection Up");

		hideKeyboard();
		userDetails();
		ageSelect("greaterthanequalto18 || lessthanequalto55", "Sep", "20", "1998", 1998, 20);
		ageCheckGreaterThanEqualTo18AndLessThanEqualTo55("greaterthanequalto18 || lessthanequalto55", "Sep", "20",
				"1998", 1998, 20);

		extent.extentLoggerPass("TC_Ring_Customer_Seg_59",
				"TC_Ring_Customer_Seg_59 - To verify users provided age exact 18 years");
		extent.extentLoggerPass("TC_Ring_Customer_Seg_60",
				"TC_Ring_Customer_Seg_60 - To verify users provided age exact 55 years");
		extent.extentLoggerPass("TC_Ring_Customer_Seg_61",
				"TC_Ring_Customer_Seg_61 - To verify users age between 18 to 55 years");
		userDetails();
		ageSelect("lessthan18", "Oct", "12", "2010", 2010, 12);
		extent.extentLoggerPass("TC_Ring_Customer_Seg_57",
				"TC_Ring_Customer_Seg_57 - To verify users provided age <18 years");

		ageSelect("greaterthan55", "Oct", "12", "1966", 1966, 12);
		ageCheckLessThan18("greaterthan55", "Oct", "12", "1966", 1966, 12);
		extent.extentLoggerPass("TC_Ring_Customer_Seg_58",
				"TC_Ring_Customer_Seg_58 - To verify users provided age >55 years");

		userDetails();
		dateOfBirth("Oct", "12", "1995");
		Aclick(RingUserDetailPage.objRegisterBtn, "Register Button");
		waitTime(5000);
		addCurrentAddress();
		offerScreen1();

		extent.extentLoggerPass("TC_Ring_Core_78",
				"TC_Ring_Core_78 - To verify the user is able to 'Continue' with entering valid 'First Name' with all "
						+ "valid details");
		extent.extentLoggerPass("TC_Ring_Core_84",
				"TC_Ring_Core_84 - To verify the user is able to 'Continue' with entering valid 'Last Name' with all "
						+ "valid details");
		extent.extentLoggerPass("TC_Ring_Core_91",
				"TC_Ring_Core_91 - To verify the user is able to 'Continue' with entering valid 'Email Address' with all "
						+ "valid details");
		extent.extentLoggerPass("TC_Ring_Core_95",
				"TC_Ring_Core_95 - To verify whether the user is able to 'Continue' when the 'Gender' is selected "
						+ "from the drop down");
		extent.extentLoggerPass("TC_Ring_Core_100",
				"TC_Ring_Core_100 - To verify when user successfully enters all valid details and clicks on continue button");

		// New push//
		ringPayLogout();

	}

	public void merchantFlow() throws Exception {

		extent.HeaderChildNode("RingPay App Merchant Flow");
		explicitWaitVisibility(RingPayMerchantFlowPage.objScanQRCodeText, 10);
		if (verifyIsElementDisplayed(RingPayMerchantFlowPage.objScanQRCodeText,
				getTextVal(RingPayMerchantFlowPage.objScanQRCodeText, "Text"))) {
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objScanQRCodeText),
					"Scan any QR to get started");
			verifyElementPresent(RingPayMerchantFlowPage.obDontHaveQRCodeText,
					getTextVal(RingPayMerchantFlowPage.obDontHaveQRCodeText, "Text"));
			verifyElementPresent(RingPayMerchantFlowPage.objSignUpORLoginLink,
					getTextVal(RingPayMerchantFlowPage.objSignUpORLoginLink, "Text"));
			logger.info("Scanning the QR Code");
			extent.extentLogger("QR Code", "Scanning the QR Code");
			extent.extentLoggerPass("TC_Ring_Core_56",
					"TC_Ring_Core_56-To verify When User selects Enable Permission option");
		}
		verifyIsElementDisplayed(RingPayMerchantFlowPage.objCreditAtZeroPopUp,
				getTextVal(RingPayMerchantFlowPage.objCreditAtZeroPopUp, "Pop up"));
		extent.extentLoggerPass("TC_Ring_Core_57", "TC_Ring_Core_57-To verify when user Scans the QR  code");
		waitTime(3000);
		if (verifyElementPresent(RingPayMerchantFlowPage.objUseCreditLimitText,
				"Use your credit limit to complete this payment Text")) {
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objUseCreditLimitText),
					"Use your credit limit to complete this payment.");
			logger.info("Navigated to Paying to Merchant UPI details page");
			extent.extentLogger("Upi Details Page", "Navigated to Paying to Merchant UPI details page");
			verifyElementPresent(RingPayMerchantFlowPage.objPayingTo,
					getTextVal(RingPayMerchantFlowPage.objPayingTo, "Text"));
			verifyElementPresent(RingPayMerchantFlowPage.objPayTypeMethod,
					getTextVal(RingPayMerchantFlowPage.objPayTypeMethod, "Payment method"));
			verifyElementPresent(RingPayMerchantFlowPage.objUpiID,
					getTextVal(RingPayMerchantFlowPage.objUpiID, "UPI Id"));
			verifyElementPresent(RingPayMerchantFlowPage.objBenefitMsg,
					getTextVal(RingPayMerchantFlowPage.objBenefitMsg, "Text"));
			verifyElementPresent(RingPayMerchantFlowPage.objTransactionMsg,
					getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Fresh user message"));

			for (int i = 0; i <= amount.length; i++) {
				type1(RingPayMerchantFlowPage.objAmountTextField, amount[i], "Amount Field");
				String validationText = getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Text is Displayed");
				if (validationText.contains("You can pay up to")) {
					break;
				}
				logger.warn(validationText);
				extent.extentLoggerWarning("validation", validationText);
				clearField(RingPayMerchantFlowPage.objAmountTextField, "Amount text field");
			}
			click(RingPayMerchantFlowPage.objPayNowBtn, "Pay Now Button");
		} else {
			logger.info("Failed to Navigate Paying to Merchant UPI details page");
			extent.extentLogger("Upi Details Page", "Failed to Navigate Paying to Merchant UPI details page");
		}
		verifyIsElementDisplayed(RingPayMerchantFlowPage.objLoginPageHeader,
				getTextVal(RingPayMerchantFlowPage.objLoginPageHeader, "Page"));
		softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign Up / Login");
		logger.info("User redirected to Signup/Login Screen");
		extent.extentLogger("login", "User redirected to Signup/Login Screen");
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithMobileCTA,
				getTextVal(RingPayMerchantFlowPage.objContinueWithMobileCTA, "CTA"));
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithGoogleCTA,
				getTextVal(RingPayMerchantFlowPage.objContinueWithGoogleCTA, "CTA"));
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithFacebookCTA,
				getTextVal(RingPayMerchantFlowPage.objContinueWithFacebookCTA, "CTA"));

		loginMobile();
		mobileNoValidation1("8123267268");
		enterOtp("888888");
		Aclick(UserRegistrationPage.objGotItBtn, "Okay Got it button");
		logger.info("User Navigated to HomePage");
		extent.extentLoggerPass("HomePage", "User Navigated to HomePage");
		explicitWaitVisibility(RingUserDetailPage.objCrossBtn, 25);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		explicitWaitVisibility(RingLoginPage.objAvailLimitHeader, 10);
		String availLimHeader = getText(RingLoginPage.objAvailLimitHeader);
		softAssertion.assertEquals("Available Limit", availLimHeader);
		ringPayLogout();

		extent.extentLoggerPass("TC_Ring_Core_58", "TC_Ring_Core_58-To verify the Screen when the Pop up disappers");
		extent.extentLoggerPass("TC_Ring_Core_59", "TC_Ring_Core_59-To verify the merchant details ");
		extent.extentLoggerPass("TC_Ring_Core_60", "TC_Ring_Core_60-To verify the First Transcation  fee message ");
		extent.extentLoggerPass("TC_Ring_Core_61",
				"TC_Ring_Core_61-To Verify  when User cliks on  enter transaction amount on screen ");
		extent.extentLoggerPass("TC_Ring_Core_62",
				"TC_Ring_Core_62-To verify when user  tries to enter amount more than first transaction limit");
		extent.extentLoggerPass("TC_Ring_Core_63", "TC_Ring_Core_63-To Verify when user tries to enter 0 amount");
		extent.extentLoggerPass("TC_Ring_Core_64",
				"TC_Ring_Core_64-To Verify user enters valid amt. and clicks on the Pay now button on merchant detail page");

		softAssertion.assertAll();

	}

//========================================================================================================================
	public void offerScreenModule() throws Exception {
		extent.HeaderChildNode("RingPay OfferScreen");
		explicitWaitVisibility(RingPayMerchantFlowPage.objScanQRCodeText, 10);
		if (verifyIsElementDisplayed(RingPayMerchantFlowPage.objScanQRCodeText,
				getTextVal(RingPayMerchantFlowPage.objScanQRCodeText, "Text"))) {
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objScanQRCodeText),
					"Scan any QR to get started");

			verifyElementPresent(RingPayMerchantFlowPage.obDontHaveQRCodeText,
					getTextVal(RingPayMerchantFlowPage.obDontHaveQRCodeText, "Text"));
			verifyElementPresent(RingPayMerchantFlowPage.objSignUpORLoginLink,
					getTextVal(RingPayMerchantFlowPage.objSignUpORLoginLink, "Text"));
			logger.info("Scanning the QR Code");
			extent.extentLogger("QR Code", "Scanning the QR Code");

		}
		verifyIsElementDisplayed(RingPayMerchantFlowPage.objCreditAtZeroPopUp,
				getTextVal(RingPayMerchantFlowPage.objCreditAtZeroPopUp, "Pop up"));
		waitTime(3000);
		if (verifyElementPresent(RingPayMerchantFlowPage.objUseCreditLimitText,
				"Use your credit limit to complete this payment Text")) {
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objUseCreditLimitText),
					"Use your credit limit to complete this payment.");
			logger.info("Navigated to Paying to Merchant UPI details page");
			extent.extentLogger("Upi Details Page", "Navigated to Paying to Merchant UPI details page");
			verifyElementPresent(RingPayMerchantFlowPage.objPayingTo,
					getTextVal(RingPayMerchantFlowPage.objPayingTo, "Text"));
			verifyElementPresent(RingPayMerchantFlowPage.objPayTypeMethod,
					getTextVal(RingPayMerchantFlowPage.objPayTypeMethod, "Payment method"));
			verifyElementPresent(RingPayMerchantFlowPage.objUpiID,
					getTextVal(RingPayMerchantFlowPage.objUpiID, "UPI Id"));
			verifyElementPresent(RingPayMerchantFlowPage.objBenefitMsg,
					getTextVal(RingPayMerchantFlowPage.objBenefitMsg, "Text"));
			verifyElementPresent(RingPayMerchantFlowPage.objTransactionMsg,
					getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Fresh user message"));

			for (int i = 0; i <= amount.length; i++) {
				type1(RingPayMerchantFlowPage.objAmountTextField, amount[i], "Amount Field");
				String validationText = getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Text is Displayed");
				if (validationText.contains("You can pay up to")) {
					break;
				}
				logger.warn(validationText);
				extent.extentLoggerWarning("validation", validationText);
				clearField(RingPayMerchantFlowPage.objAmountTextField, "Amount text field");
			}
			click(RingPayMerchantFlowPage.objPayNowBtn, "Pay Now Button");
		} else {
			logger.info("Failed to Navigate Paying to Merchant UPI details page");
			extent.extentLogger("Upi Details Page", "Failed to Navigate Paying to Merchant UPI details page");
		}
		verifyIsElementDisplayed(RingPayMerchantFlowPage.objLoginPageHeader,
				getTextVal(RingPayMerchantFlowPage.objLoginPageHeader, "Page"));
		softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign Up / Login");
		logger.info("User redirected to Signup/Login Screen");
		extent.extentLogger("login", "User redirected to Signup/Login Screen");
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithMobileCTA,
				getTextVal(RingPayMerchantFlowPage.objContinueWithMobileCTA, "CTA"));
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithGoogleCTA,
				getTextVal(RingPayMerchantFlowPage.objContinueWithGoogleCTA, "CTA"));
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithFacebookCTA,
				getTextVal(RingPayMerchantFlowPage.objContinueWithFacebookCTA, "CTA"));

		loginMobile();
		mobileNoValidation1("9" + RandomIntegerGenerator(9));
		enterOtp("888888");
		userDetails();
		dateOfBirth("Feb", "10", "1996");
		Aclick(RingUserDetailPage.objRegisterBtn, "Register Button");
		addCurrentAddress();
		waitTime(5000);
		merchantOfferPageValidation();
		acceptAndPay();
		ringPayLogout();
	}

//=================================================================================================================================
	public void promoCodeModule() throws Exception {
		extent.HeaderChildNode("Promo code Module");

		explicitWaitVisibility(RingPromoCodeLogin.objPromoCodePageHeaderText, 10);
		WebElement QRPromoCodeLogin = getDriver().findElement(RingPromoCodeLogin.objPromoCodePageHeaderText);
		verifyElementExist1(QRPromoCodeLogin, getText(RingPromoCodeLogin.objPromoCodePageHeaderText));
		type(RingPromoCodeLogin.objEnterAmt, "1", "Amount Field");
		String rupeeSymbol = getText(RingPromoCodeLogin.objRupeeSymbol);
		String amount = getText(RingPromoCodeLogin.objEnterAmt);
		logger.info(rupeeSymbol + amount);
		extent.extentLogger("Amount", rupeeSymbol + amount);
		verifyElementPresentAndClick(RingPromoCodeLogin.objPayBtn, getText(RingPromoCodeLogin.objPayBtn));
		explicitWaitVisibility(RingPromoCodeLogin.objLoginPageHeader, 10);
		verifyElementPresent(RingPromoCodeLogin.objLoginPageHeader, getText(RingPromoCodeLogin.objLoginPageHeader));
	}

	public void userDetails() throws Exception {
		extent.HeaderChildNode("Age Criteria");

		explicitWaitVisibility(RingUserDetailPage.objFirstName, 10);
		click(RingUserDetailPage.objFirstName, "First Name Field");
		type(RingUserDetailPage.objFirstName, "Sunil", "First Name field");

		explicitWaitVisibility(RingUserDetailPage.objLastName, 10);
		click(RingUserDetailPage.objLastName, "Last Name Field");
		type(RingUserDetailPage.objLastName, "Chatla", "Last Name field");

		explicitWaitVisibility(RingUserDetailPage.objMothersName, 10);
		click(RingUserDetailPage.objMothersName, "Mothe's Name Field");
		type(RingUserDetailPage.objMothersName, "Mom", "Mother's Name field");
		hideKeyboard();

		explicitWaitVisibility(RingUserDetailPage.objEmail, 10);
		click(RingUserDetailPage.objEmail, "Email Field");
		click(RingUserDetailPage.objNone, "None of the Above Button");
		String email = generateRandomString(8) + "@gmail.com";
		type(RingUserDetailPage.objEmail, email, "Email Filed");
		hideKeyboard();
		genderSelect("male");
	}

	public void ageSelect(String key, String Month, String Date, String Year, int year, int date) throws Exception {
		extent.HeaderChildNode("Age Criteria");

		switch (key) {
		case "lessthan18":
			dateOfBirth(Month, Date, Year);

			age = ageCal(year, date);
			break;

		case "greaterthan55":
			dateOfBirth(Month, Date, Year);

			age = ageCal(year, date);
			break;

		case "greaterthanequalto18 || lessthanequalto55":
			dateOfBirth(Month, Date, Year);

			age = ageCal(year, date);
			break;

		default:
			logger.info("invalid age!!");
			break;
		}

		waitTime(3000);
		click(RingUserDetailPage.objRegisterBtn, "Register Button");

		if (age < 18) {
			waitTime(10000);
			logger.warn("The present age is <18 therefore the age is: " + age);
			extent.extentLoggerWarning("age", "The present age is <18 therefore the age is: " + age);
			waitTime(10000);
			verifyElementPresent(RingUserDetailPage.objAgeLessThan18Notification,
					getText(RingUserDetailPage.objAgeLessThan18Notification));
			logger.info("Age Criteria Failed");
			extent.extentLogger("Age Verification", "Age Criteria Failed");

		} else if (age > 55) {

			logger.warn("The present age is >55 therefore the age is: " + age);
			extent.extentLoggerWarning("age", "The present age is >55 therefore the age is: " + age);
			waitTime(10000);
			verifyElementPresent(RingUserDetailPage.objSorryMsg, getText(RingUserDetailPage.objSorryMsg));
			logger.info("Age Criteria Failed");
			extent.extentLogger("Age Verification", "Age Criteria Failed");
			verifyElementPresentAndClick(RingUserDetailPage.objHamburgerTab, "Hamburger Tab");
			verifyElementPresentAndClick(RingUserDetailPage.objProfileTabCompletedPercentage,
					"Profile Completed Percentage tab");
			verifyElementPresentAndClick(RingUserDetailPage.objLogoutBtn, "Logout Button");
			logger.info("Are you sure you want to Logout?");
			explicitWaitVisibility(RingUserDetailPage.objLogOutYesBtn, 10);
			click(RingUserDetailPage.objLogOutYesBtn, "Yes Button");
			// ringPayLogout();
		} else if (age >= 18 || age <= 55) {
			waitTime(10000);
			addCurrentAddress();
			logger.info("The present age is >=18 & <=55 and therefor the age is: " + age);
			extent.extentLogger("age", "The present age is >=18 & <=55 and therefor the age is: " + age);
//			offerScreen();
			logger.info("Age Criteria Passed");
			extent.extentLogger("Age Verification", "Age Criteria Passed");

			verifyElementPresent(PromoCodeOfferPage.objOffer, "Offer");
			String sOffer = getText(PromoCodeOfferPage.objOffer);
			softAssertion.assertEquals(sOffer, "Offer");
			verifyElementPresent(PromoCodeOfferPage.objAmount,
					getText(PromoCodeOfferPage.objAmount) + "Amount Payed in Paying to banner");
			verifyElementPresent(PromoCodeOfferPage.objUpiIdMerchant,
					getText(PromoCodeOfferPage.objUpiIdMerchant) + "UPI Id in Paying to banner");
			verifyElementPresent(PromoCodeOfferPage.objRepaymentDateText,
					getText(PromoCodeOfferPage.objRepaymentDateText) + getText(PromoCodeOfferPage.objrepaymentDate));
			verifyElementPresent(PromoCodeOfferPage.objApprovalLimitText,
					getTextVal(PromoCodeOfferPage.objApprovalLimitText, "Text"));
			verifyElementPresent(PromoCodeOfferPage.objApprovalLimitAmount,
					getTextVal(PromoCodeOfferPage.objApprovalLimitAmount, "is Approval Limit Amount"));
			verifyElementPresent(PromoCodeOfferPage.objFeeDetails,
					getTextVal(PromoCodeOfferPage.objFeeDetails, "Text"));
			verifyElementPresent(PromoCodeOfferPage.objCheckBox, "Check Box");
			verifyElementPresent(PromoCodeOfferPage.objTermsAndCondition, "Terms & Condition");
			verifyElementPresent(PromoCodeOfferPage.objAcceptOffer,
					getTextVal(PromoCodeOfferPage.objAcceptOffer, "Text"));
			offerScreen();
			logger.info(
					"TC_Ring_Core_161-Offer screen should be displayed to the user with Offer heading, amount with upi id and payable amount, Repayment date, limit banner, terms and conditions text with link for t&c checkbox and Accept offer button is displayed");
			extent.extentLoggerPass("TC_Ring_Core_161",
					"TC_Ring_Core_161-Offer screen should be displayed to the user with Offer heading, amount with upi id and payable amount, Repayment date, limit banner, terms and conditions text with link for t&c checkbox and Accept offer button is displayed");
			ringPayLogout();
		}
	}

	public void offerScreen() throws Exception {
		waitTime(10000);
		verifyElementPresent(RingUserDetailPage.objOfferPageHeader, getText(RingUserDetailPage.objOfferPageHeader));
		verifyElementPresentAndClick(RingUserDetailPage.objIAcceptCheckBox,
				"I accept the ring's Terms & Conditions and IT Act 2000. checkbox");
		verifyElementPresentAndClick(RingUserDetailPage.objAcceptOfferBtn, "Accept Button");
		explicitWaitVisibility(RingUserDetailPage.objSetPinHeader, 10);
		verifyElementPresent(RingUserDetailPage.objSetPinHeader, "Set Pin Page");
		click(RingUserDetailPage.objEnterPin, "Enter Pin Field");
		type(RingUserDetailPage.objEnterPin, "1234", "Enter pin Field");
		click(RingUserDetailPage.objReEnterPin, "Re-Enter Pin Field");
		type(RingUserDetailPage.objReEnterPin, "1234", "Re-Enter pin Field");
		verifyElementPresentAndClick(RingUserDetailPage.objSubmitBtn,
				getText(RingUserDetailPage.objSubmitBtn) + "Button");
		verifyElementPresentAndClick(RingLoginPage.objHomePageBtn, "Home Page button");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		verifyElementPresentAndClick(RingLoginPage.objNoBtn, "No button");
	}

	public void offerScreen1() throws Exception {
		waitTime(10000);
		verifyElementPresent(RingUserDetailPage.objOfferPageHeader, getText(RingUserDetailPage.objOfferPageHeader));
		verifyElementPresent(TermsAndConditionPage.objEligibleMsg,
				getText(TermsAndConditionPage.objEligibleMsg) + getText(TermsAndConditionPage.objEligibleAmt));
		verifyElementPresent(TermsAndConditionPage.objFirstBillBannerMsg,
				getText(TermsAndConditionPage.objFirstBillBannerMsg));
		verifyElementPresent(TermsAndConditionPage.objIAcceptMsg, getText(TermsAndConditionPage.objIAcceptMsg));
		verifyElementPresentAndClick(RingUserDetailPage.objIAcceptCheckBox,
				"I accept the ring's Terms & Conditions and IT Act 2000. checkbox");
		verifyElementPresentAndClick(RingUserDetailPage.objAcceptOfferBtn, "Accept Button");

		explicitWaitVisibility(RingUserDetailPage.objSetPinHeader, 10);
		verifyElementPresent(RingUserDetailPage.objSetPinHeader, "Set Pin Page");
		click(RingUserDetailPage.objEnterPin, "Enter Pin Field");
		type(RingUserDetailPage.objEnterPin, "1234", "Enter pin Field");
		click(RingUserDetailPage.objReEnterPin, "Re-Enter Pin Field");
		type(RingUserDetailPage.objReEnterPin, "1234", "Re-Enter pin Field");
		verifyElementPresentAndClick(RingUserDetailPage.objSubmitBtn,
				getText(RingUserDetailPage.objSubmitBtn) + "Button");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");

	}

	public void ageCheckLessThan18(String key, String Month, String Date, String Year1, int year1, int date)
			throws Exception {
		extent.HeaderChildNode(key);
		explicitWaitVisibility(RingLoginPage.objQrCodeHeader, 10);
		if (verifyElementPresent(RingLoginPage.objQrCodeHeader, "Don't have QR Code header text")) {
			logger.info("Don't have QR Code header");
			extent.extentLoggerPass("Don't have QR Code header", "Don't have QR Code header is displayed");

			if (verifyElementPresent(RingLoginPage.objLoginLink, "Signup/Login link")) {
				logger.info("Signup/Login link");
				Aclick(RingLoginPage.objLoginLink, "Signup/Login link");

				explicitWaitVisibility(RingLoginPage.objLoginHeader, 10);
				if (verifyElementPresent(RingLoginPage.objLoginHeader, "Signup/Login header")) {
					logger.info("Signup/Login Header");
					String loginHeaderTxt = getText(RingLoginPage.objLoginHeader);
					Assert.assertEquals(loginHeaderTxt, "Sign Up / Login");
					extent.extentLoggerPass("Signup/Login header", "Signup/Login header is displayed");

					loginMobile();

					click(RingLoginPage.objMobTextField, "Mobile text field");
					String phoneNo = "9" + RandomIntegerGenerator(9);
					type(RingLoginPage.objMobTextField, phoneNo, "Mobile text field");

					enterOtp("888888");

				}
			}
		}
	}

	public void ageCheckGreaterThanEqualTo18AndLessThanEqualTo55(String key, String Month, String Date, String Year2,
			int year2, int date) throws Exception {
		extent.HeaderChildNode("Age >=18 & <=55");
		explicitWaitVisibility(RingLoginPage.objQrCodeHeader, 10);
		if (verifyElementPresent(RingLoginPage.objQrCodeHeader, "Don't have QR Code header text")) {
			logger.info("Don't have QR Code header");
			extent.extentLoggerPass("Don't have QR Code header", "Don't have QR Code header is displayed");

			if (verifyElementPresent(RingLoginPage.objLoginLink, "Signup/Login link")) {
				logger.info("Signup/Login link");
				Aclick(RingLoginPage.objLoginLink, "Signup/Login link");

				explicitWaitVisibility(RingLoginPage.objLoginHeader, 10);
				if (verifyElementPresent(RingLoginPage.objLoginHeader, "Signup/Login header")) {
					logger.info("Signup/Login Header");
					String loginHeaderTxt = getText(RingLoginPage.objLoginHeader);
					Assert.assertEquals(loginHeaderTxt, "Sign Up / Login");
					extent.extentLoggerPass("Signup/Login header", "Signup/Login header is displayed");

					loginMobile();

					click(RingLoginPage.objMobTextField, "Mobile text field");
					String phoneNo = "9" + RandomIntegerGenerator(9);
					type(RingLoginPage.objMobTextField, phoneNo, "Mobile text field");

					enterOtp("888888");
				}
			}
		}

	}

	public void cameraPermission() throws Exception {
		explicitWaitVisibility(RingLoginPage.objCamPermHeader, 30);
		verifyElementPresent(RingLoginPage.objCamPermHeader, "Camera Permission required");
		String camPermHeaderTxt = getText(RingLoginPage.objCamPermHeader);
		softAssertion.assertEquals(camPermHeaderTxt, "Camera Permission required");
		logger.info("Camera Permission required popup");
	}

	public void enablePermissions() throws Exception {
		explicitWaitVisibility(RingLoginPage.objCamPermPopUp, 10);
		Aclick(RingLoginPage.objCamPermPopUp, "Enable permissions button");
		logger.info("Foreground allow camera permissions");
		extent.extentLoggerPass("Foreground allow camera permissions", "Foreground allow camera permissions options");
		explicitWaitVisibility(RingLoginPage.objAllowCamera, 10);
		Aclick(RingLoginPage.objAllowCamera, "While using the app foreground camera permission option");

		explicitWaitVisibility(RingLoginPage.objQrCodeHeader, 10);
		logger.info("Don't have QR Code header");
		String qrCodeHeader = getText(RingLoginPage.objQrCodeHeader);
		softAssertion.assertEquals(qrCodeHeader, "Don't have a QR code?");
	}

	public void loginMobile() throws Exception {
		explicitWaitVisibility(RingLoginPage.objLoginMobile, 10);
		Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");

		trueCallerPopup();
		if (verifyElementPresent(RingLoginPage.objNoneBtn, "None of the above button")) {
			String noneOfAboveTxt = getText(RingLoginPage.objNoneBtn);
			softAssertion.assertEquals(noneOfAboveTxt, "NONE OF THE ABOVE");
			Aclick(RingLoginPage.objNoneBtn, "None of the above");

			explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
			logger.info("Verify Mobile Header");
			String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
			softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
			explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
			explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
			logger.info("Verify Mobile Header");
			String verifyMobHeaderTxt1 = getText(RingLoginPage.objVerifyMobHeader);
			softAssertion.assertEquals(verifyMobHeaderTxt1, "Verify Mobile");
			explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
		}

		else {
			explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
			logger.info("Verify Mobile Header");
			String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
			softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
			explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
			explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
			logger.info("Verify Mobile Header");
			String verifyMobHeaderTxt1 = getText(RingLoginPage.objVerifyMobHeader);
			softAssertion.assertEquals(verifyMobHeaderTxt1, "Verify Mobile");
			explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
		}

	}

	public void trueCallerPopup() throws Exception {
		if (verifyElementPresent(RingLoginPage.objTruSkipBtn, "True caller popup")) {
			Aclick(RingLoginPage.objTruSkipBtn, "True caller skip button");
		}
		if (verifyElementPresent(RingLoginPage.objNoneBtn, "None of the above")) {
			Aclick(RingLoginPage.objNoneBtn, "None of the above");
		}
	}

	public String mobileNoValidation(String mobNo) throws Exception {
		// public String noTxt;
		waitTime(4000);
		if (verifyElementPresent(RingLoginPage.objTruSkipBtn, "True caller popup")) {
			Aclick(RingLoginPage.objTruSkipBtn, "True caller skip button");
		}
		if (verifyElementPresent(RingLoginPage.objNoneBtn, "None of the above button")) {
			String noneOfAboveTxt = getText(RingLoginPage.objNoneBtn);
			softAssertion.assertEquals(noneOfAboveTxt, "NONE OF THE ABOVE");
			Aclick(RingLoginPage.objNoneBtn, "None of the above");
		}
		explicitWaitVisibility(RingLoginPage.objVerifyMobHeader, 10);
		logger.info("Verify Mobile Header");
		String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
		softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
		waitTime(4000);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
		System.out.println(mobNo);
		waitTime(5000);
		String noTxt = getText(RingLoginPage.objMobTextField);
		if (!verifyIsElementDisplayed(RingLoginPage.objNextBtn)) {
			logger.info("Navigated to OTP Page");
		} else {
			Aclick(RingLoginPage.objNextBtn, "Next Button");
		}
		return noTxt;
	}

	public String mobileNoValidation1(String mobNo) throws Exception {
		waitTime(4000);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
		System.out.println(mobNo);
		String noTxt = getText(RingLoginPage.objMobTextField);
		if (!verifyElementExist(RingLoginPage.objNextBtn, "Next Button")) {
			logger.info("Navigated to OTP Page");
		} else {
			click(RingLoginPage.objNextBtn, "Next Button");
		}
		Aclick(MobileLoginPage.txtResendOtp, "Resend OTP");
		return noTxt;
	}

	public void enterOtp(String otp) throws Exception {
		// explicitWaitVisibility(RingLoginPage.OtpAutoRead, 10);
		explicitWaitClickable(RingLoginPage.resendOtpTxt, 10);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, otp, "Enter OTP");
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void gmailLogin(String userId, String password) throws Exception {
		extent.HeaderChildNode("User Play store Flow Module");
		cameraPermission();
		enablePermissions();
		explicitWaitVisibility(SignUP_LoginPage.continueWithGmail, 20);
		Aclick(SignUP_LoginPage.continueWithGmail, "Continue With Gmail Account");
		if (verifyElementPresent(GmailLoginPage.addAnotherAccount, "Add Another Account")) {
			Aclick(GmailLoginPage.addAnotherAccount, "Add Another Account");
		}

		explicitWaitVisibility(GmailLoginPage.enterEmailID, 20);
		Aclick(GmailLoginPage.enterEmailID, "Enter Email Field");
		type(GmailLoginPage.enterEmailID, userId, "Entered Email ID");
		Aclick(GmailLoginPage.nextButton, "Next Button");
		explicitWaitVisibility(GmailLoginPage.enterPassword, 20);
		// Aclick(GmailLoginPage.enterPassword, "Enter Password Field");
		type(GmailLoginPage.enterPassword, password, "Password");
		Aclick(GmailLoginPage.nextButton, "Next Button");

		if (verifyElementPresent(GmailLoginPage.txtKeepYourAccountUpdate,
				"Keep Your account updated with phone number")) {
			scrollToBottomOfPage();
			Aclick(GmailLoginPage.btnYesImIn, "Yes I'm in");
		}

		explicitWaitVisibility(GmailLoginPage.btnIAgree, 20);
		Aclick(GmailLoginPage.btnIAgree, "I Agree Button");
		explicitWaitVisibility(GmailLoginPage.txtGoogleServices, 20);

		Aclick(GmailLoginPage.btnMore, "More Button");
		explicitWaitVisibility(GmailLoginPage.btnAccept, 20);
		Aclick(GmailLoginPage.btnAccept, "Accept Button");
		waitTime(10000);
	}

	/**
	 * 
	 * @param mobNo
	 * @param otp
	 * @throws Exception
	 */

	// TC28
	public void otpTimerStart(String mobNo, String otp) throws Exception {
		extent.HeaderChildNode("OTP Timer Start With Caption OTP Box Disable");
		cameraPermission();
		enablePermissions();
		explicitWaitVisibility(RingLoginPage.objLoginMobile, 10);
		Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");
		explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
		hideKeyboard();
		explicitWaitVisibility(MobileLoginPage.txtEnterOTP, 20);
		String getAutoreadValidation = getText(MobileLoginPage.txtAutoReadingOTP);
		softAssertion.assertEquals("Auto Reading OTP", getAutoreadValidation);
		logger.info("Auto Reading OTP Validate " + getAutoreadValidation);
		extent.extentLoggerPass("Auto Reading OTP", "Auto Reading OTP Validate Successfully");
		explicitWaitVisibility(MobileLoginPage.txtOtpTimeStamp, 20);
		String otpTimeStamp = getText(MobileLoginPage.txtOtpTimeStamp);
		otpTimeStamp = otpTimeStamp.substring(0, 5);
		System.out.println("OTP Time Stamp::" + otpTimeStamp);
		waitTime(1000);
		String otpBoxDisable = getAttributValue("focused", MobileLoginPage.enterOTPNumberFiled);
		System.out.println("Element Focus:: " + otpBoxDisable);
		softAssertion.assertEquals("00:0", otpTimeStamp);
		if (otpBoxDisable.equals("false")) {
			softAssertion.assertEquals("false", otpBoxDisable);
			logger.info("OTP Box Disable");
			extent.extentLoggerPass("OTP Box Disable", "OTP Box Disable");
		} else {
			logger.info("OTP Box Enabled");
			extent.extentLoggerFail("OTP Box Enable", "OTP Box Enabled");
		}
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, otp, "Enter OTP");
		waitTime(10000);
	}

	// TC29

	public void blockUserAfter_5_Attemp(String mobNo, String otp) throws Exception {
		extent.HeaderChildNode("Block Number For 2 Minutes If Attemp More Than 5 Time");
		cameraPermission();
		enablePermissions();
		explicitWaitVisibility(RingLoginPage.objLoginMobile, 10);
		Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");
		explicitWaitVisibility(RingLoginPage.objMobTextField, 10);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
		hideKeyboard();
		explicitWaitVisibility(MobileLoginPage.txtEnterOTP, 20);
		String getAutoreadValidation = getText(MobileLoginPage.txtAutoReadingOTP);
		softAssertion.assertEquals("Auto Reading OTP", getAutoreadValidation);
		logger.info("Auto Reading OTP Validate " + getAutoreadValidation);
		extent.extentLoggerPass("Auto Reading OTP", "Auto Reading OTP Validate Successfully");
		explicitWaitVisibility(MobileLoginPage.txtOtpTimeStamp, 20);
		String otpTimeStamp = getText(MobileLoginPage.txtOtpTimeStamp);
		otpTimeStamp = otpTimeStamp.substring(0, 5);
		System.out.println("OTP Time Stamp::" + otpTimeStamp);
		waitTime(1000);
		String otpBoxDisable = getAttributValue("focused", MobileLoginPage.enterOTPNumberFiled);
		System.out.println("Element Focus:: " + otpBoxDisable);
		softAssertion.assertEquals("00:0", otpTimeStamp);
		if (otpBoxDisable.equals("false")) {
			softAssertion.assertEquals("false", otpBoxDisable);
			logger.info("OTP Box Disable");
			extent.extentLoggerPass("OTP Box Disable", "OTP Box Disable");
		} else {
			logger.info("OTP Box Enabled");
			extent.extentLoggerFail("OTP Box Enable", "OTP Box Enabled");
		}
		explicitWaitVisibility(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, otp, "Enter OTP");
		waitTime(10000);
	}

	public void dateOfBirth(String month, String date, String year) throws Exception {
		explicitWaitClickable(UserRegistrationPage.objUserDOB, 10);
		Aclick(UserRegistrationPage.objUserDOB, "DOB field");
//		Aclick(UserRegistrationPage.objUserDOB, "DOB field");
		if (!verifyElementDisplayed(UserRegistrationPage.objGenderCancelBtn)) {
			Aclick(UserRegistrationPage.objUserDOB, "DOB field");
		}

		explicitWaitVisibility(UserRegistrationPage.objDatePickerMonth, 10);
		Aclick(UserRegistrationPage.objDatePickerMonth, "Month field");
		clearField(UserRegistrationPage.objDatePickerMonth, "Month field");
		type(UserRegistrationPage.objDatePickerMonth, month, "Month field");

		Aclick(UserRegistrationPage.objDatePickerDate, "Date field");
		clearField(UserRegistrationPage.objDatePickerDate, "Date field");
		type(UserRegistrationPage.objDatePickerDate, date, "Date field");

		Aclick(UserRegistrationPage.objDatePickerYear, "Year field");
		clearField(UserRegistrationPage.objDatePickerYear, "Year field");
		type(UserRegistrationPage.objDatePickerYear, year, "Year field");

		Aclick(UserRegistrationPage.objOK, "OK button");

	}

	public void genderSelect(String gender) throws Exception {
		waitTime(5000);
		click(UserRegistrationPage.objGenderSelect, "Gender dropdown");
		click(UserRegistrationPage.objGenderSelect, "Gender dropdown");

		waitTime(3000);
		if (gender.equalsIgnoreCase("Male")) {
			explicitWaitVisibility(UserRegistrationPage.objMale, 10);
			click(UserRegistrationPage.objMale, "Male gender");
			hideKeyboard();
		}

		else {
			explicitWaitVisibility(UserRegistrationPage.objFemale, 10);
			Aclick(UserRegistrationPage.objFemale, "Female gender");
			hideKeyboard();
		}
	}

	
	
	/**
	 * Business method for RingPay Application Logout
	 * 
	 */

	public void ringPayLogout() throws Exception {
		extent.HeaderChildNode("RingPay Logout");

		explicitWaitVisibility(RingLoginPage.objTopMenu, 15);
		Aclick(RingLoginPage.objTopMenu, "Top left menu button");

		explicitWaitVisibility(RingLoginPage.objProfileSelect, 10);
		Aclick(RingLoginPage.objProfileSelect, "Profile Select Button");

		explicitWaitVisibility(RingLoginPage.objLogoutBtn, 10);

		Aclick(RingLoginPage.objLogoutBtn, "Logout Button");

		explicitWaitVisibility(RingLoginPage.objLogoutTxt, 10);
		String logoutTxt = getText(RingLoginPage.objLogoutTxt);
		Assert.assertEquals(logoutTxt, "Are you sure you want to logout?");
		logger.info("Logout popup comes up");
		extent.extentLoggerPass("Logout popup", "Logout popup comes up");

		Aclick(RingLoginPage.objYesBtn, "Yes confirmation button");

		explicitWaitVisibility(RingLoginPage.objQrCodeHeader, 10);
		String logoutConfTxt = getText(RingLoginPage.objQrCodeHeader);
		Assert.assertEquals(logoutConfTxt, "Don't have a QR code?");
		logger.info("User is successfully logged out");
		extent.extentLoggerPass("Logout confirmation", "User is successfully logged out");

	}

	public void hamburgerTab() throws Exception {
		verifyElementPresentAndClick(RingUserDetailPage.objHamburgerTab, "Hamburger Tab");
		verifyElementPresentAndClick(RingUserDetailPage.objProfileTabCompletedPercentage,
				"Profile Completed Percentage tab");
		verifyElementPresentAndClick(RingUserDetailPage.objLogoutBtn, "Logout Button");
		logger.info("Are you sure you want to Logout?");
		explicitWaitVisibility(RingUserDetailPage.objLogOutYesBtn, 10);
		click(RingUserDetailPage.objLogOutYesBtn, "Yes Button");
	}

//=================================================================================================================================
	public void merchantOfferPageValidation() throws Exception {
		verifyElementPresent(PromoCodeOfferPage.objOffer, "Offer");
		String sOffer = getText(PromoCodeOfferPage.objOffer);
		softAssertion.assertEquals(sOffer, "Offer");
		verifyElementPresent(PromoCodeOfferPage.objAmount,
				getText(PromoCodeOfferPage.objAmount) + "Amount Payed in Paying to banner");
		verifyElementPresent(PromoCodeOfferPage.objUpiIdMerchant,
				getText(PromoCodeOfferPage.objUpiIdMerchant) + "UPI Id in Paying to banner");
		verifyElementPresent(PromoCodeOfferPage.objRepaymentDateText,
				getText(PromoCodeOfferPage.objRepaymentDateText) + getText(PromoCodeOfferPage.objrepaymentDate));
		verifyElementPresent(PromoCodeOfferPage.objApprovalLimitText,
				getTextVal(PromoCodeOfferPage.objApprovalLimitText, "Text"));
		verifyElementPresent(PromoCodeOfferPage.objApprovalLimitAmount,
				getTextVal(PromoCodeOfferPage.objApprovalLimitAmount, "is Approval Limit Amount"));
		verifyElementPresent(PromoCodeOfferPage.objFeeDetails, getTextVal(PromoCodeOfferPage.objFeeDetails, "Text"));
		verifyElementPresent(PromoCodeOfferPage.objCheckBox, "Check Box");
		verifyElementPresent(PromoCodeOfferPage.objTermsAndCondition, "Terms & Condition");
		verifyElementPresent(PromoCodeOfferPage.objAcceptOffer, getTextVal(PromoCodeOfferPage.objAcceptOffer, "Text"));
		logger.info("TC_Ring_Core_160 , Offer Page of PromoCode flow Validated ");
		extent.extentLoggerPass("TC_Ring_Core_160", "TC_Ring_Core_160,Validation of offer Page of PromoCode flow");
		FeeDetailsValidation();
		TAndCLinkValidation();
	}

	public void FeeDetailsValidation() throws Exception {
		extent.HeaderChildNode("Fee Details");
		verifyElementPresent(PromoCodeOfferPage.objFeeDetails, getTextVal(PromoCodeOfferPage.objFeeDetails, "Text"));
		Aclick(PromoCodeOfferPage.objFeeDetails, "Fee Details");
		logger.info("Fee Details Popup is Displayed");
		extent.extentLoggerPass("Fee Details ", "Fee Details Popup is Displayed");
		if (verifyElementPresent(FeeDetailsPage.objTransactionFee, "Transaction Fee")) {
			verifyElementPresent(FeeDetailsPage.objFeeDetailsPopup, "Fee Details Popup");
			verifyElementPresent(FeeDetailsPage.objTransactionFeePercentage,
					getTextVal(FeeDetailsPage.objTransactionFeePercentage, "is Transaction fee Percentage"));
			logger.info("TC_Ring_Core_162 , Fee Details Link Validated");
			extent.extentLoggerPass("TC_Ring_Core_162", "C_Ring_Core_162,Validation of Fee Details Link");
			Back(1);
		}
	}

	public void TAndCLinkValidation() throws Exception {
		extent.HeaderChildNode("T&C Link Validation");
		verifyElementPresent(PromoCodeOfferPage.objTermsAndCondition,
				getTextVal(PromoCodeOfferPage.objTermsAndCondition, "Text"));
		Aclick(PromoCodeOfferPage.objTermsAndCondition, "Terms And Conditions");
		waitTime(5000);
		if (verifyElementPresent(TermsAndConditionPage.objTAndCondition,
				getTextVal(TermsAndConditionPage.objTAndCondition, "Text"))) {
			logger.info("TC_Ring_Core_163,Navigated to Terms And Condition Web Page");
			extent.extentLoggerPass("TC_Ring_Core_163",
					"TC_Ring_Core_163,Terms And Conditions are displayed in Web Page");
			Back(1);
		}
	}

//====================Address Flow Module==============================================================================
	public void addCurrentAddress() throws Exception {
		extent.HeaderChildNode("RingPay Add Address Flow");
		if (verifyElementPresent(AddAddressPage.objAddressHeader, "Add Current Address Page Header")) {
			extent.extentLoggerPass("TC_Ring_Core_101", "TC_Ring_Core_101 - To verfiy Address page");

			verifyElementExist(AddAddressPage.objRoomNoTextField,
					getTextVal(AddAddressPage.objRoomNoTextField, "Text Field"));
			click(AddAddressPage.objRoomNoTextField, "Room No Field");
//	            type(AddAddressPage.objRoomNoField, "", getTextVal(AddAddressPage.objMandatoryWarnMessage, "Warning Message"));
			Aclick(AddAddressPage.objSubmitButton, "Submit Button");
			click(AddAddressPage.objRoomNoTextField, "Room No Field");
			type(AddAddressPage.objRoomNoField, "@#%^&#$", "Room No. Field");
			logger.info(getTextVal(AddAddressPage.objSpclCharWarnMessage, "Warning Message"));
			extent.extentLoggerPass("Warning Message",
					getTextVal(AddAddressPage.objSpclCharWarnMessage, "Warning Message"));
			clearField(AddAddressPage.objRoomNoField, "Room No Field");
			type(AddAddressPage.objRoomNoTextField, "86", "Room No. Field");
			extent.extentLoggerPass("Warning Message",
					"TC_Ring_Core_102 - To verify room no and building name field with valid data");

			verifyElementExist(AddAddressPage.objAddressLineOneTextField,
					getTextVal(AddAddressPage.objAddressLineOneTextField, "Text Field"));
			click(AddAddressPage.objAddressLineOneTextField, "Address Line 1 Field");
//	            type(AddAddressPage.objAddressLineOneField, "", getTextVal(AddAddressPage.objMandatoryWarnMessage, "Warning Message"));
			Aclick(AddAddressPage.objSubmitButton, "Submit Button");
			click(AddAddressPage.objAddressLineOneField, "Address Line 1 Field");
			type(AddAddressPage.objAddressLineOneField, "@#%^&#$", "Address Line 1");
			logger.info(getTextVal(AddAddressPage.objMinThreeCharWarnMessage, "Warning Message"));
			extent.extentLoggerPass("Warning Message",
					getTextVal(AddAddressPage.objMinThreeCharWarnMessage, "Warning Message"));
			clearField(AddAddressPage.objAddressLineOneField, "Address Line 1 Field");
			type(AddAddressPage.objAddressLineOneField, "44, Borivali", "Address Line 1");
			extent.extentLoggerPass("TC_Ring_Core_103", "TC_Ring_Core_103 - To verify address 1 field with valid data");

			verifyElementExist(AddAddressPage.objAddressLineTwoTextField,
					getTextVal(AddAddressPage.objAddressLineTwoTextField, "Field"));
			click(AddAddressPage.objAddressLineTwoField, "Address Line 2 Field");
//	            type(AddAddressPage.objAddressLineTwoField, "", getTextVal(AddAddressPage.objMandatoryWarnMessage, "Warning Message"));
			Aclick(AddAddressPage.objSubmitButton, "Submit Button");
			click(AddAddressPage.objAddressLineTwoField, "Address Line 2 Field");
			type(AddAddressPage.objAddressLineTwoField, "@#%^&#$", "Address Line 2");
			logger.info(getTextVal(AddAddressPage.objSpclCharWarnMessage, "Warning Message"));
			extent.extentLoggerPass("Warning Message",
					getTextVal(AddAddressPage.objSpclCharWarnMessage, "Warning Message"));
			clearField(AddAddressPage.objAddressLineTwoField, "Address Line 2 Field");
			type(AddAddressPage.objAddressLineTwoField, "Vasa Street",
					getTextVal(AddAddressPage.objAddressLineTwoField, "Text Field"));
			extent.extentLoggerPass("TC_Ring_Core_104", "TC_Ring_Core_104 - To verify address 2 field with valid data");

			hideKeyboard();

			verifyElementExist(AddAddressPage.objLandmarkTextField,
					getTextVal(AddAddressPage.objLandmarkTextField, "Field"));
			click(AddAddressPage.objLandmarkTextFields, "Landmark Field");
//	            type(AddAddressPage.objLandmarkTextFields, "", getTextVal(AddAddressPage.objMandatoryWarnMessage, "Warning Message"));
			Aclick(AddAddressPage.objSubmitButton, "Submit Button");
			click(AddAddressPage.objLandmarkTextFields, "Landmark Field");
			type(AddAddressPage.objLandmarkTextFields, "@#%^&#$", "Landmark Field");
			logger.info(getTextVal(AddAddressPage.objMinThreeCharWarnMessage, "Warning Message"));
			extent.extentLoggerPass("Warning Message",
					getTextVal(AddAddressPage.objMinThreeCharWarnMessage, "Warning Message"));
			clearField(AddAddressPage.objLandmarkTextFields, "Landmark Field");
			type(AddAddressPage.objLandmarkTextFields, "Das Gupta street",
					getTextVal(AddAddressPage.objLandmarkTextFields, "Text Field"));
			extent.extentLoggerPass("TC_Ring_Core_105", "TC_Ring_Core_105 - To verify Landmark field with valid data");

			hideKeyboard();

			Swipe("up", 2);
			verifyElementExist(AddAddressPage.objPincodeTextField,
					getTextVal(AddAddressPage.objPincodeTextField, "Text Field"));
			click(AddAddressPage.objPinCodeField, "Pincode Field");
//	            type(AddAddressPage.objPinCodeField, "", getTextVal(AddAddressPage.objMandatoryWarnMessage, "Warning Message"));
			Aclick(AddAddressPage.objSubmitButton, "Submit Button");
			click(AddAddressPage.objPinCodeField, "Pincode Field");
			type(AddAddressPage.objPinCodeField, "255552", "Pincode Field");
			logger.info(getTextVal(AddAddressPage.objInvalidPinCode, "Warning Message"));
			extent.extentLoggerPass("Warning Message", getTextVal(AddAddressPage.objInvalidPinCode, "Warning Message"));
			clearField(AddAddressPage.objPinCodeField, "PinCode Field");
			type(AddAddressPage.objPinCodeField, "400080", getTextVal(AddAddressPage.objPinCodeField, "Text Field"));

			hideKeyboard();

			verifyElementExist(AddAddressPage.objCityTextField, getTextVal(AddAddressPage.objCityTextField, "Field"));
			explicitWaitVisibility(AddAddressPage.objCityTextField, 10);
			logger.info(getTextVal(AddAddressPage.objCityTextField, "City Name autoPopulated after entering pincode"));
			extent.extentLogger("CityName",
					getTextVal(AddAddressPage.objCityTextField, "City Name autoPopulated after entering pincode"));

			verifyElementExist(AddAddressPage.objStateTextField, getTextVal(AddAddressPage.objStateTextField, "Field"));
			explicitWaitVisibility(AddAddressPage.objStateTextField, 10);
			logger.info(
					getTextVal(AddAddressPage.objStateTextField, "State Name autoPopulated after entering pincode"));
			extent.extentLogger("CityName",
					getTextVal(AddAddressPage.objStateTextField, "State Name autoPopulated after entering pincode"));
			extent.extentLoggerPass("TC_Ring_Core_106,TC_Ring_Core_107,",
					"TC_Ring_Core_106,TC_Ring_Core_107 - To verify pincode field with invalid and valid pincode");

			explicitWaitVisibility(AddAddressPage.objSubmitButton, 10);
			Aclick(AddAddressPage.objSubmitButton, "Submit Button");
			extent.extentLoggerPass("TC_Ring_Core_108",
					"TC_Ring_Core_108 - To verfiy user clicks on submit button after entering valid address details");
		} else {
			logger.warn("Address Page is not diaplyed");
			extent.extentLoggerWarning("Address Page", "Address Page is not displyed");
		}
	}

	public void acceptAndPay() throws Exception {
		verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,
				getText(TermsAndConditionPage.objAcceptText));
		String toastMsg = getText(TermsAndConditionPage.objToastMsg);
		waitTime(3000);
		logger.info("TC_Ring_Core_164 " + toastMsg);
		extent.extentLoggerPass("TC_Ring_Core_164", "TC_Ring_Core_164 " + toastMsg);
		verifyElementPresentAndClick(RingUserDetailPage.objIAcceptCheckBox, "Terms and Condition Check Box");
		setWifiConnectionToONOFF("Off");
		verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,
				getText(TermsAndConditionPage.objAcceptText));
		logger.info("TC_Ring_Core_165- User be able to make payment by clicking on 'Accept & Pay'");
		extent.extentLoggerPass("TC_Ring_Core_165",
				"TC_Ring_Core_165- User be able to make payment by clicking on 'Accept & Pay'");
		waitTime(5000);
		setWifiConnectionToONOFF("On");
		Aclick(TermsAndConditionPage.objOkBtn, "Button Ok, Got It!");
		logger.info("TC_Ring_Core_166- Check internet connection screen displayed to the user");
		extent.extentLoggerPass("TC_Ring_Core_166",
				"TC_Ring_Core_166- Check internet connection screen displayed to the user");
		setWifiConnectionToONOFF("On");
		verifyElementPresentAndClick(RingUserDetailPage.objIAcceptCheckBox, "Terms and Condition Check Box");
		logger.info("TC_Ring_Core_167- User should be able to make payment by clicking on 'Accept & Pay'");
		extent.extentLoggerPass("TC_Ring_Core_167",
				"TC_Ring_Core_167-User should be able to make payment by clicking on 'Accept & Pay'");

		getDriver().runAppInBackground(Duration.ofSeconds(10));
		waitTime(5000);
		verifyElementPresent(TermsAndConditionPage.objAcceptAndPayBtn, getText(TermsAndConditionPage.objAcceptText));
		logger.info("TC_Ring_Core_171- When relaunch the app user should displayed accept and pay");
		extent.extentLoggerPass("TC_Ring_Core_171",
				"TC_Ring_Core_171-When relaunch the app user should displayed accept and pay");

		waitTime(5000);
		setLocationConnectionToONOFF("Off");
		verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,
				getText(TermsAndConditionPage.objAcceptText));
		verifyElementPresent(TermsAndConditionPage.objGpsAllowBtn,
				getText(TermsAndConditionPage.objGpsAllowBtn) + " Button");
		verifyElementPresent(TermsAndConditionPage.objNotNowBtn,
				getText(TermsAndConditionPage.objNotNowBtn) + " Button");
		logger.info("TC_Ring_Core_168- User should be shown location on popup with allow and not now option");
		extent.extentLoggerPass("TC_Ring_Core_168",
				"TC_Ring_Core_168-User should be shown location on popup with allow and not now option");

		verifyElementPresentAndClick(TermsAndConditionPage.objNotNowBtn, getText(TermsAndConditionPage.objNotNowBtn));
		verifyElementPresent(PromoCodeOfferPage.objOffer, "Offer");
		softAssertion.assertEquals(getText(PromoCodeOfferPage.objOffer), "Offer");
		logger.info("TC_Ring_Core_170- User should not be allowed to proceed unless he allows");
		extent.extentLoggerPass("TC_Ring_Core_170",
				"TC_Ring_Core_170-User should not be allowed to proceed unless he allows");

		verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,
				getText(TermsAndConditionPage.objAcceptText));
		verifyElementPresentAndClick(TermsAndConditionPage.objGpsAllowBtn,
				getText(TermsAndConditionPage.objGpsAllowBtn));
		Aclick(UserRegistrationPage.objOK, "OK button");
		verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,
				getText(TermsAndConditionPage.objAcceptText));
		verifyElementPresent(TermsAndConditionPage.objSetPin, getText(TermsAndConditionPage.objSetPin));
		softAssertion.assertEquals(getText(TermsAndConditionPage.objSetPin), "Set Pin");

		SetPin();

		logger.info(
				"TC_Ring_Core_169- User should be be able to accept and pay and redirect to transaction set pin page");
		extent.extentLoggerPass("TC_Ring_Core_169",
				"TC_Ring_Core_169-User should be be able to accept and pay and redirect to transaction set pin page");

		logger.info("TC_Ring_Core_173- User is redirected to transaction pin setup page");
		extent.extentLoggerPass("TC_Ring_Core_173",
				"TC_Ring_Core_173-User is redirected to transaction pin setup page");

		logger.info("TC_Ring_Core_174-App should redirect to SET PIN screen");
		extent.extentLoggerPass("TC_Ring_Core_174", "TC_Ring_Core_174-App should redirect to SET PIN screen");

		logger.info("TC_Ring_Core_175-App should redirect to SET PIN screen");
		extent.extentLoggerPass("TC_Ring_Core_175", "TC_Ring_Core_175-App should redirect to SET PIN screen");
		softAssertion.assertAll();
	}
//==================================================================================================================================

	public void SetPin() throws Exception {
//		extent.HeaderChildNode("Set Pin Page Validation");
		String sSetPin = getText(UserRegistrationPage.objSetPin);
		softAssertion.assertEquals(sSetPin, "Set Pin");
		if (verifyElementPresent(UserRegistrationPage.objEnterPin1, "Enter Pin first Element")) {
			Aclick(UserRegistrationPage.objEnterPin1, "Enter pin first Element");
			type(UserRegistrationPage.objEnterPin1, "1", "Enter pin first Element");
			Aclick(UserRegistrationPage.objSubmit, "Submit Button");
			hideKeyboard();
			verifyElementPresent(UserRegistrationPage.objErrorMessage,
					getTextVal(UserRegistrationPage.objErrorMessage, "Error Message"));
			softAssertion.assertEquals(UserRegistrationPage.objErrorMessage, "Please enter new Pin");
			logger.info("TC_Ring_Core_177,Please enter new Pin Error Message is displayed");
			extent.extentLoggerPass("TC_Ring_Core_177",
					"TC_Ring_Core_177,Please enter new Pin Error Message is displayed");
		}
		if (verifyElementPresent(UserRegistrationPage.objEnterPin2, "Enter Pin Second Element")) {
			Aclick(UserRegistrationPage.objEnterPin2, "Enter pin Second Element");
			type(UserRegistrationPage.objEnterPin2, "1", "Enter pin Second Element");
			hideKeyboard();
			verifyElementPresent(UserRegistrationPage.objErrorMessage,
					getTextVal(UserRegistrationPage.objErrorMessage, "Error Message"));
			softAssertion.assertEquals(UserRegistrationPage.objErrorMessage, "Please enter new Pin");
			logger.info("TC_Ring_Core_178,Please enter new Pin Error Message is displayed");
			extent.extentLoggerPass("TC_Ring_Core_178",
					"TC_Ring_Core_178,Please enter new Pin Error Message is displayed");
		}
		if (verifyElementPresent(UserRegistrationPage.objEnterPin3, "Enter Pin Third Element")) {
			Aclick(UserRegistrationPage.objEnterPin3, "Enter pin Third Element");
			type(UserRegistrationPage.objEnterPin3, "1", "Enter pin Third Element");
			hideKeyboard();
			verifyElementPresent(UserRegistrationPage.objErrorMessage,
					getTextVal(UserRegistrationPage.objErrorMessage, "Error Message"));
			softAssertion.assertEquals(UserRegistrationPage.objErrorMessage, "Please enter new Pin");
			logger.info("TC_Ring_Core_179,Please enter new Pin Error Message is displayed");
			extent.extentLoggerPass("TC_Ring_Core_179",
					"TC_Ring_Core_179,Please enter new Pin Error Message is displayed");
		}
		waitTime(3000);
		Aclick(UserRegistrationPage.objEnterPin4, "Enter pin Fourth Element");
		type(UserRegistrationPage.objEnterPin4, "1", "Enter pin Fourth Element");
		hideKeyboard();
		if (verifyElementPresent(UserRegistrationPage.objHide, "asterisk")) {
			for (int nPin = 1; nPin >= 4; nPin++) {
				String sAsterisk = getText(UserRegistrationPage.objHide(nPin));
				softAssertion.assertEquals(sAsterisk, "*");
			}
			logger.info("TC_Ring_Core_180,Pin should displayed as asterisk mark");
			extent.extentLoggerPass("TC_Ring_Core_180", "TC_Ring_Core_180,Pin should displayed as asterisk mark");
		}
		if (verifyElementPresent(UserRegistrationPage.objEnterPin, "Enter pin Data")) {
			String sEnteredPin = getText(UserRegistrationPage.objEnterPin);
			int nEnterPin = Integer.parseInt(sEnteredPin);
			softAssertion.assertEquals(sEnteredPin, 1111, "Return True When Both are Integers");
			logger.info("TC_Ring_Core_176, Entered Pin " + nEnterPin + " is an Integer");
			extent.extentLoggerPass("TC_Ring_Core_176", "TC_Ring_Core_176,Entered Pin " + nEnterPin + " is an Integer");
		}
		if (verifyElementPresent(UserRegistrationPage.objReEnterPin, "Re-Enter Pin")) {
			Aclick(UserRegistrationPage.objReEnterPin, "Re-Enter Pin");
			type(UserRegistrationPage.objReEnterPin, "1234", "Re-Enter Pin");
			Aclick(UserRegistrationPage.objSubmit, "Submit Button");
			verifyElementPresent(UserRegistrationPage.objSorryErrorMessage,
					getTextVal(UserRegistrationPage.objSorryErrorMessage, "Error Message"));
			softAssertion.assertEquals(UserRegistrationPage.objSorryErrorMessage,
					"Sorry! Pin does not match, please enter again");
			logger.info("TC_Ring_Core_181,Sorry! Pin does not match, please enter again Error Message is displayed");
			extent.extentLoggerPass("TC_Ring_Core_181",
					"TC_Ring_Core_181,Sorry! Pin does not match, please enter again Error Message is displayed");
		}

		if (verifyElementPresent(UserRegistrationPage.objReEnterPin, "Re-Enter Pin")) {
			Aclick(UserRegistrationPage.objReEnterPin, "Re-Enter Pin");
			clearField(UserRegistrationPage.objReEnterPin, "Re-Enter Pin");
			type(UserRegistrationPage.objReEnterPin, "1111", "Re-Enter Pin");

			explicitWaitVisibility(UserRegistrationPage.objSubmit, 10);
			Aclick(UserRegistrationPage.objSubmit, "Submit Button");
			verifyElementPresent(UserRegistrationPage.objPaymentDoneMsg,
					getText(UserRegistrationPage.objPaymentDoneMsg));
			verifyElementPresent(UserRegistrationPage.objSuccessMsg,
					getText(UserRegistrationPage.objSuccessMsg) + " credit limit!");
			click(UserRegistrationPage.objGoTOHomePageBtn, "Got to HomePage Button");
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			verifyElementPresentAndClick(UserRegistrationPage.objNoBtn, getText(UserRegistrationPage.objNoBtn));
			String sHome = getText(HomePage.objHome);
			softAssertion.assertEquals(sHome, "Home");
			ringPayLogout();
			logger.info("TC_Ring_Core_182, Navigated to Home Page");
			extent.extentLoggerPass("TC_Ring_Core_182", "TC_Ring_Core_182,Navigated to Home Page");

			if (verifyIsElementDisplayed(RingPayMerchantFlowPage.objScanQRCodeText,
					getTextVal(RingPayMerchantFlowPage.objScanQRCodeText, "Text"))) {
				softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objScanQRCodeText),
						"Scan any QR to get started");
				
				verifyElementPresent(RingPayMerchantFlowPage.obDontHaveQRCodeText,
						getTextVal(RingPayMerchantFlowPage.obDontHaveQRCodeText, "Text"));
				verifyElementPresent(RingPayMerchantFlowPage.objSignUpORLoginLink,
						getTextVal(RingPayMerchantFlowPage.objSignUpORLoginLink, "Text"));
				logger.info("Scanning the QR Code");
				extent.extentLogger("QR Code", "Scanning the QR Code");

			}
			verifyIsElementDisplayed(RingPayMerchantFlowPage.objCreditAtZeroPopUp,
					getTextVal(RingPayMerchantFlowPage.objCreditAtZeroPopUp, "Pop up"));
			waitTime(3000);
			if (verifyElementPresent(RingPayMerchantFlowPage.objUseCreditLimitText,
					"Use your credit limit to complete this payment Text")) {
				softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objUseCreditLimitText),
						"Use your credit limit to complete this payment.");
				logger.info("Navigated to Paying to Merchant UPI details page");
				extent.extentLogger("Upi Details Page", "Navigated to Paying to Merchant UPI details page");
				verifyElementPresent(RingPayMerchantFlowPage.objPayingTo,
						getTextVal(RingPayMerchantFlowPage.objPayingTo, "Text"));
				verifyElementPresent(RingPayMerchantFlowPage.objPayTypeMethod,
						getTextVal(RingPayMerchantFlowPage.objPayTypeMethod, "Payment method"));
				verifyElementPresent(RingPayMerchantFlowPage.objUpiID,
						getTextVal(RingPayMerchantFlowPage.objUpiID, "UPI Id"));
				verifyElementPresent(RingPayMerchantFlowPage.objBenefitMsg,
						getTextVal(RingPayMerchantFlowPage.objBenefitMsg, "Text"));
				verifyElementPresent(RingPayMerchantFlowPage.objTransactionMsg,
						getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Fresh user message"));

				for (int i = 0; i <= amount.length; i++) {
					type1(RingPayMerchantFlowPage.objAmountTextField, amount[i], "Amount Field");
					String validationText = getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Text is Displayed");
					if (validationText.contains("You can pay up to")) {
						break;
					}
					logger.warn(validationText);
					extent.extentLoggerWarning("validation", validationText);
					clearField(RingPayMerchantFlowPage.objAmountTextField, "Amount text field");
				}
				click(RingPayMerchantFlowPage.objPayNowBtn, "Pay Now Button");
			} else {
				logger.info("Failed to Navigate Paying to Merchant UPI details page");
				extent.extentLogger("Upi Details Page", "Failed to Navigate Paying to Merchant UPI details page");
			}
			verifyIsElementDisplayed(RingPayMerchantFlowPage.objLoginPageHeader,
					getTextVal(RingPayMerchantFlowPage.objLoginPageHeader, "Page"));
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign Up / Login");
			logger.info("User redirected to Signup/Login Screen");
			extent.extentLogger("login", "User redirected to Signup/Login Screen");
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithMobileCTA,
					getTextVal(RingPayMerchantFlowPage.objContinueWithMobileCTA, "CTA"));
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithGoogleCTA,
					getTextVal(RingPayMerchantFlowPage.objContinueWithGoogleCTA, "CTA"));
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithFacebookCTA,
					getTextVal(RingPayMerchantFlowPage.objContinueWithFacebookCTA, "CTA"));

			loginMobile();
			mobileNoValidation1("9" + RandomIntegerGenerator(9));
			enterOtp("888888");
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			Aclick(RingUserDetailPage.objRegisterBtn, "Register Button");
			addCurrentAddress();
			waitTime(5000);
			verifyElementPresentAndClick(RingUserDetailPage.objIAcceptCheckBox, "Terms and Condition Check Box");
			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,
					getText(TermsAndConditionPage.objAcceptText));

			verifyElementPresent(RingUserDetailPage.objSetPinHeader, "Set Pin Page");
			click(RingUserDetailPage.objEnterPin, "Enter Pin Field");
			type(RingUserDetailPage.objEnterPin, "1234", "Enter pin Field");
			click(RingUserDetailPage.objReEnterPin, "Re-Enter Pin Field");
			type(RingUserDetailPage.objReEnterPin, "1234", "Re-Enter pin Field");
			setLocationConnectionToONOFF("off");
			verifyElementPresentAndClick(RingUserDetailPage.objSubmitBtn,getText(RingUserDetailPage.objSubmitBtn) + "Button");
			verifyElementPresent(TermsAndConditionPage.objGpsAllowBtn,getText(TermsAndConditionPage.objGpsAllowBtn) + " Button");
			verifyElementPresent(TermsAndConditionPage.objNotNowBtn,getText(TermsAndConditionPage.objNotNowBtn) + " Button");
			logger.info("TC_Ring_Core_183 , Location permission Required Popup is displayed");
			extent.extentLoggerPass("TC_Ring_Core_183","TC_Ring_Core_183 , Location permission Required Popup is displayed");

			verifyElementPresentAndClick(TermsAndConditionPage.objGpsAllowBtn,getText(TermsAndConditionPage.objGpsAllowBtn) + " Button");
			click(UserRegistrationPage.objOK, "OK button");
			verifyElementPresentAndClick(RingUserDetailPage.objSubmitBtn,getText(RingUserDetailPage.objSubmitBtn) + "Button");
			verifyElementPresentAndClick(RingLoginPage.objHomePageBtn, "Home Page button");
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			verifyElementPresentAndClick(RingLoginPage.objNoBtn, "No button");
			ringPayLogout();
			logger.info("TC_Ring_Core_185, Transaction pin is set successful also user completes the first transaction");
			extent.extentLoggerPass("TC_Ring_Core_185","TC_Ring_Core_185, Transaction pin is set successful also user completes the first transaction");

			explicitWaitVisibility(RingPayMerchantFlowPage.objScanQRCodeText, 10);
			if (verifyIsElementDisplayed(RingPayMerchantFlowPage.objScanQRCodeText,getTextVal(RingPayMerchantFlowPage.objScanQRCodeText, "Text"))) {
				softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objScanQRCodeText),"Scan any QR to get started");
				
				verifyElementPresent(RingPayMerchantFlowPage.obDontHaveQRCodeText,getTextVal(RingPayMerchantFlowPage.obDontHaveQRCodeText, "Text"));
				verifyElementPresent(RingPayMerchantFlowPage.objSignUpORLoginLink,getTextVal(RingPayMerchantFlowPage.objSignUpORLoginLink, "Text"));
				logger.info("Scanning the QR Code");
				extent.extentLogger("QR Code", "Scanning the QR Code");

			}
			verifyIsElementDisplayed(RingPayMerchantFlowPage.objCreditAtZeroPopUp,
					getTextVal(RingPayMerchantFlowPage.objCreditAtZeroPopUp, "Pop up"));
			waitTime(3000);
			if (verifyElementPresent(RingPayMerchantFlowPage.objUseCreditLimitText,"Use your credit limit to complete this payment Text")) {
				softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objUseCreditLimitText),"Use your credit limit to complete this payment.");
				logger.info("Navigated to Paying to Merchant UPI details page");
				extent.extentLogger("Upi Details Page", "Navigated to Paying to Merchant UPI details page");
				verifyElementPresent(RingPayMerchantFlowPage.objPayingTo,getTextVal(RingPayMerchantFlowPage.objPayingTo, "Text"));
				verifyElementPresent(RingPayMerchantFlowPage.objPayTypeMethod,getTextVal(RingPayMerchantFlowPage.objPayTypeMethod, "Payment method"));
				verifyElementPresent(RingPayMerchantFlowPage.objUpiID,getTextVal(RingPayMerchantFlowPage.objUpiID, "UPI Id"));
				verifyElementPresent(RingPayMerchantFlowPage.objBenefitMsg,getTextVal(RingPayMerchantFlowPage.objBenefitMsg, "Text"));
				verifyElementPresent(RingPayMerchantFlowPage.objTransactionMsg,getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Fresh user message"));

				for (int i = 0; i <= amount.length; i++) {
					type1(RingPayMerchantFlowPage.objAmountTextField, amount[i], "Amount Field");
					String validationText = getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Text is Displayed");
					if (validationText.contains("You can pay up to")) {
						break;
					}
					logger.warn(validationText);
					extent.extentLoggerWarning("validation", validationText);
					clearField(RingPayMerchantFlowPage.objAmountTextField, "Amount text field");
				}
				click(RingPayMerchantFlowPage.objPayNowBtn, "Pay Now Button");
			} else {
				logger.info("Failed to Navigate Paying to Merchant UPI details page");
				extent.extentLogger("Upi Details Page", "Failed to Navigate Paying to Merchant UPI details page");
			}
			verifyIsElementDisplayed(RingPayMerchantFlowPage.objLoginPageHeader,getTextVal(RingPayMerchantFlowPage.objLoginPageHeader, "Page"));
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign Up / Login");
			logger.info("User redirected to Signup/Login Screen");
			extent.extentLogger("login", "User redirected to Signup/Login Screen");
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithMobileCTA,getTextVal(RingPayMerchantFlowPage.objContinueWithMobileCTA, "CTA"));
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithGoogleCTA,getTextVal(RingPayMerchantFlowPage.objContinueWithGoogleCTA, "CTA"));
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithFacebookCTA,getTextVal(RingPayMerchantFlowPage.objContinueWithFacebookCTA, "CTA"));

			loginMobile();
			mobileNoValidation1("9" + RandomIntegerGenerator(9));
			enterOtp("888888");
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			Aclick(RingUserDetailPage.objRegisterBtn, "Register Button");
			addCurrentAddress();
			waitTime(5000);
			verifyElementPresentAndClick(RingUserDetailPage.objIAcceptCheckBox, "Terms and Condition Check Box");
			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,getText(TermsAndConditionPage.objAcceptText));

			verifyElementPresent(RingUserDetailPage.objSetPinHeader, "Set Pin Page");
			click(RingUserDetailPage.objEnterPin, "Enter Pin Field");
			type(RingUserDetailPage.objEnterPin, "1234", "Enter pin Field");
			click(RingUserDetailPage.objReEnterPin, "Re-Enter Pin Field");
			type(RingUserDetailPage.objReEnterPin, "1234", "Re-Enter pin Field");
			setLocationConnectionToONOFF("off");
			verifyElementPresentAndClick(RingUserDetailPage.objSubmitBtn,getText(RingUserDetailPage.objSubmitBtn) + "Button");
			if (verifyElementPresent(UserRegistrationPage.objAllowLocationPopup, "Location Permission Required Text")) {
				verifyElementPresent(UserRegistrationPage.objNotNowLocationBtn,getTextVal(UserRegistrationPage.objNotNowLocationBtn, "Text"));
				Aclick(UserRegistrationPage.objNotNowLocationBtn, "Not Now Button");
				Back(2);
//				verifyElementPresentAndClick(RingUserDetailPage.objSubmitBtn,getText(RingUserDetailPage.objSubmitBtn) + "Button");
				verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
				verifyElementPresent(HomePage.objTransactionfailMsg, getText(HomePage.objTransactionfailMsg));
				verifyElementPresentAndClick(RingLoginPage.objNoBtn, "No button");
				String Home = getText(HomePage.objHome);
				softAssertion.assertEquals(Home, "Home");
				ringPayLogout();
				logger.info("TC_Ring_Core_186, Transaction pin is not set and Navigated to Home Page");
				extent.extentLoggerPass("TC_Ring_Core_186","TC_Ring_Core_186,Transaction pin is not set and Navigated to Home Page");
				System.out.println("-----------------------------------------------------------");
			}
		}
	}
	
	//==================================================================================================================================	
	public void repaymentMultipleCases() throws Exception{
		extent.HeaderChildNode("Check payment page multiple cases");
		
		cameraPermission();
		enablePermissions();
		explicitWaitVisibility(RingLoginPage.objLoginLink,10);
		Aclick(RingLoginPage.objLoginLink,"Signup/Login link");
		loginMobile();
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, "8123267268", "Mobile text field");
		enterOtp("888888");
		explicitWaitVisibility(RingLoginPage.objReadAcceptBtn,10);
		Aclick(RingLoginPage.objReadAcceptBtn, "Read & Accept button");

		explicitWaitVisibility(RingLoginPage.objLocAccess, 10);

		Aclick(RingLoginPage.objLocAccess, "Location Access option");
		Aclick(RingLoginPage.objPhoneAccess, "Phone access option");
		Aclick(RingLoginPage.objSMSAccess, "SMS access option");
		
		explicitWaitVisibility(HomePage.objAdHeader, 10);
		Aclick(HomePage.objAdCloseBtn, "AD Close button");
		
		explicitWaitClickable(HomePage.objPayEarlyBtn,10);
		Aclick(HomePage.objPayEarlyBtn,"Pay Early Button");
		explicitWaitVisibility(HomePage.objRepaymentHeader,10);
		waitTime(3000);
		Aclick(HomePage.objAmtToBeRadio,"Amount to be paid radio button");
		waitTime(3000);
		hideKeyboard();
		explicitWaitVisibility(HomePage.objNetBankingOption, 10);
		Aclick(HomePage.objNetBankingOption,"Net Banking/Debit card option");
		explicitWaitVisibility(HomePage.objFirstError,10);
		String firstErrorTxt = getText(HomePage.objFirstError);
		softAssertion.assertEquals("Please enter amount", firstErrorTxt);
		extent.extentLoggerPass("TC_Ring_Payment_228",
				"TC_Ring_Payment_228 - Verify payment page by keeping other amount field empty");
		
		Aclick(HomePage.objAmtRepayText,"Amount repay text field");
		type(HomePage.objAmtRepayText,"0","Amount repay text field");
		hideKeyboard();
		Aclick(HomePage.objNetBankingOption,"Net Banking/Debit card option");
		explicitWaitVisibility(HomePage.objSecondError,10);
		String secondErrorTxt = getText(HomePage.objSecondError);
		softAssertion.assertEquals("Minimum amount should be ₹1", secondErrorTxt);
		extent.extentLoggerPass("TC_Ring_Payment_229",
				"TC_Ring_Payment_229 - Verify payment page by entering amount as 0 in other amount field");
		
		clearField(HomePage.objAmtRepayText,"Amount repay text field");
		
		Aclick(HomePage.objAmtRepayText,"Amount repay text field");
		type(HomePage.objAmtRepayText,"7777","Amount repay text field");
		hideKeyboard();
		Aclick(HomePage.objNetBankingOption,"Net Banking/Debit card option");
		explicitWaitVisibility(HomePage.objThirdError,10);
		String thirdErrorTxt = getText(HomePage.objThirdError);
		softAssertion.assertEquals("Amount is greater than payable amount.", thirdErrorTxt);
		extent.extentLoggerPass("TC_Ring_Payment_230",
				"TC_Ring_Payment_230 - Verify payment page by entering amount greater than Total Payable Amount");
		
		clearField(HomePage.objAmtRepayText,"Amount repay text field");
		Aclick(HomePage.objAmtRepayText,"Amount repay text field");
		type(HomePage.objAmtRepayText,"10,20,","Amount repay text field");
		hideKeyboard();
		Aclick(HomePage.objNetBankingOption,"Net Banking/Debit card option");
		explicitWaitVisibility(HomePage.objFourthError,10);
		String fourthErrorTxt = getText(HomePage.objFourthError);
		softAssertion.assertEquals("Please enter valid amount", fourthErrorTxt);
		extent.extentLoggerPass("TC_Ring_Payment_231",
				"TC_Ring_Payment_231 - Verify payment page by entering invalid amount as \"10,20,\" in other amount field");
		
		clearField(HomePage.objAmtRepayText,"Amount repay text field");
		Aclick(HomePage.objAmtRepayText,"Amount repay text field");
		type(HomePage.objAmtRepayText,"17","Amount repay text field");
		hideKeyboard();
		Aclick(HomePage.objNetBankingOption,"Net Banking/Debit card option");
		waitTime(3000);
		Aclick(HomePage.objNetBankingOption,"Net Banking/Debit card option");
		explicitWaitVisibility(HomePage.objNetbanking,10);
		Aclick(HomePage.objNetbanking,"Netbanking");
		explicitWaitVisibility(HomePage.objSelectBankHeader,10);
		Aclick(HomePage.objSBIBank,"SBI bank");
		Aclick(HomePage.objPayNowBtn,"Pay Now Button");
		explicitWaitVisibility(HomePage.objPayFail,10);
		extent.extentLoggerPass("TC_Ring_Payment_232",
				"TC_Ring_Payment_232 - Verify payment page by entering amount less than 20rs if Partner is RAZORPAY or PAYNIMO");
		Aclick(HomePage.objTryAgain,"Try Again button");
		for(int i =0;i<=2;i++) {
			Back(i);
		}
		
		
		explicitWaitVisibility(HomePage.objRepaymentHeader,10);
		Aclick(HomePage.objAmtRepayText,"Amount to be paid text field");
		clearField(HomePage.objAmtRepayText,"Amount to be paid text field");
		type(HomePage.objAmtRepayText,"20","Amount to be paid text field");
		hideKeyboard();
		Aclick(HomePage.objNetBankingOption,"Net Banking/Debit card option");
		waitTime(3000);
		Aclick(HomePage.objNetBankingOption,"Net Banking/Debit card option");
		explicitWaitVisibility(HomePage.objNetbanking,10);
		extent.extentLoggerPass("TC_Ring_Payment_233",
				"TC_Ring_Payment_233 - Verify payment page by entering amount equal to 20rs");
		Aclick(HomePage.objNetbanking,"Netbanking");
		explicitWaitVisibility(HomePage.objSelectBankHeader,10);
		Aclick(HomePage.objSBIBank,"SBI bank");
		Aclick(HomePage.objPayNowBtn,"Pay Now Button");
		explicitWaitVisibility(HomePage.objSuccessBtn,10);
		Aclick(HomePage.objSuccessBtn,"Success Button");
		explicitWaitVisibility(HomePage.objRepaySuccess,10);
		Aclick(HomePage.objHomePage,"Go to homepage button");
		explicitWaitVisibility(RingLoginPage.objAvailLimitHeader,10);
		extent.extentLoggerPass("TC_Ring_Payment_234",
				"TC_Ring_Payment_234 - To Verify user clicks on View Details on Payment screen");
		
		
	}
}