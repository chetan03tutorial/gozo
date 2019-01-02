package com.lbg.ib.api.sales.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.filters.securityfilter.crypto.StringEncrypter;
import com.lloydstsb.ea.filters.securityfilter.crypto.exceptions.EncryptionException;
import com.lloydstsb.ea.filters.securityfilter.crypto.scheme.DesEdeEncryptionScheme;

@Component
public class ArrangementDecoder {

    @Autowired
    private LoggerDAO logger;
    private static final String ENCRYPTION_KEY = "l3dkg+ths*erklg9463hdke7";
    

    public String getDecryptedParam(String encryptedKey){
        String decryptedKey = null;
        try {
            StringEncrypter encrypter = new StringEncrypter(DesEdeEncryptionScheme.getInstance(), ENCRYPTION_KEY);
            decryptedKey = encrypter.decrypt(encryptedKey);
        } catch (EncryptionException e) {
            logger.traceLog(ArrangementDecoder.class, e);
            throw new ServiceException(new ResponseError(ResponseErrorConstants.ENCRYPTION_ERROR,"Error while decrypting the key " + encryptedKey));
        }
        return decryptedKey;
    }
    
    public String getEncryptedParam(String encryptedKey){
        String decryptedKey = null;
        try {
            StringEncrypter encrypter = new StringEncrypter(DesEdeEncryptionScheme.getInstance(), ENCRYPTION_KEY);
            decryptedKey = encrypter.encrypt(encryptedKey);
        } catch (EncryptionException e) {
            logger.traceLog(ArrangementDecoder.class, e);
            throw new ServiceException(new ResponseError(ResponseErrorConstants.ENCRYPTION_ERROR,"Error while decrypting the key " + encryptedKey));
        }
        return decryptedKey;
    }
    
}
