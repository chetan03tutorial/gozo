package com.lbg.ib.api.sales.utils;

import static com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.USER_NOT_FOUND;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.ea.filters.securityfilter.crypto.StringEncrypter;
import com.lloydstsb.ea.filters.securityfilter.crypto.exceptions.EncryptionException;
import com.lloydstsb.ea.filters.securityfilter.crypto.scheme.DesEdeEncryptionScheme;

@Component
public class DecoderUtil {

    @Autowired
    private LoggerDAO logger;
    private static final String ENCRYPTION_KEY = "l3dkg+ths*erklg9463hdke7";
    /**
     * Object of resolver.
     */
    @Autowired
    private GalaxyErrorCodeResolver resolver;

    /**
     * Decrypt the string.
     * @param encryptedKey
     * @return
     */
    @TraceLog
    public String getDecryptedParam(String encryptedKey) {
        logger.traceLog(this.getClass(), "Decryption starts: " + encryptedKey);
        String decryptedKey = null;
        try {
            StringEncrypter encrypter = new StringEncrypter(DesEdeEncryptionScheme.getInstance(), ENCRYPTION_KEY);
            decryptedKey = encrypter.decrypt(encryptedKey);
        } catch (EncryptionException e) {
            logger.traceLog(this.getClass(), "Error while decrypting the key " + encryptedKey);
            logger.logException(this.getClass(), e);
            throw new ServiceException(resolver.resolve(USER_NOT_FOUND));
        }
        return decryptedKey;
    }

}
