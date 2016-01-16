package io.kowalski.claptrap.models.proprietary;

import com.twilio.sdk.verbs.Sms;

import io.kowalski.claptrap.models.SMS;

public class SMSConverter {

    /**
     * Converts the Claptrap SMS to the selected proprietary instance.
     * @param sms to convert
     * @param smsOutputType kind of sms object to output
     */
    public static final Object convert(final SMS sms, final SMSEnum smsOutputType) {
        switch (smsOutputType) {
        case AWS_SMS:
            throw new IllegalStateException("Unimplemented SMS Output Type selected.");
        case TWILLO:
            return twillo(sms);
        default:
            throw new IllegalStateException("Unimplemented SMS Output Type selected.");
        }
    }

    private static final Object aws(final SMS sms) {
        return null;
    }

    private static final Sms twillo(final SMS sms) {
        final Sms twilloSms = new Sms(sms.getMessage());
        twilloSms.setFrom(sms.getFrom());
        twilloSms.setTo(sms.getTo());

        return twilloSms;
    }

}
