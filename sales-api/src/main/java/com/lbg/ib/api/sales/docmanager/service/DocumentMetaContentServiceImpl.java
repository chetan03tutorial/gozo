package com.lbg.ib.api.sales.docmanager.service;

import com.lbg.ib.api.sales.docmanager.dao.DocumentMetaContentDAO;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfoResponse;
import com.lbg.ib.api.sales.docmanager.domain.DocumentMetaContentResponse;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Service class for the document meta content
 * @author 8903735
 *
 */
@Component
public class DocumentMetaContentServiceImpl implements DocumentMetaContentService {

	private static final Object VALID_STRING = "VALID";

	@Autowired
	private DocumentMetaContentDAO documentMetaContentDAO;

	@Autowired
	private GalaxyErrorCodeResolver resolver;

	@Autowired
	private LoggerDAO logger;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	/**
	 * This method returns the document meta content for the given partyId
	 * @param ocisId
	 * @return {@link DocumentMetaContentResponse}
	 */
	public DocumentMetaContentResponse retrieveDocumentMetaContent(String ocisId) {
		DAOResponse<DocumentMetaContentResponse> documentMetaContentDAOResponse = documentMetaContentDAO
				.retrieveDocumentMetaContent(ocisId);

		DAOResponse.DAOError error = documentMetaContentDAOResponse.getError();
		if (error != null) {
			throw new ServiceException(resolver.customResolve(error.getErrorCode(),
					error.getErrorMessage()));
		}
		return documentMetaContentDAOResponse.getResult();
	}

	@TraceLog
	public CustomerDocumentInfoResponse recordDocumentMetaContent(CustomerDocumentInfo customerDocumentInfo) {
		logger.traceLog(this.getClass(), "Inside recordDocumentMetaContent. Record document - " + customerDocumentInfo);
		setAdditionalInfoWithDocumentDate(customerDocumentInfo);

		//persist the documents to OCIS now
		logger.traceLog(this.getClass(), "Inside recordDocumentMetaContent. Persist the document with OCIS Id - " + customerDocumentInfo.getOcisId());
		DAOResponse<CustomerDocumentInfoResponse> customerDocumentInfoDAOResponse = documentMetaContentDAO
				.recordDocumentMetaContentFromSOA(customerDocumentInfo);
		DAOResponse.DAOError error = customerDocumentInfoDAOResponse.getError();
		if (error != null) {
			throw new ServiceException(resolver.customResolve(error.getErrorCode(), error.getErrorMessage()));
		}

		return customerDocumentInfoDAOResponse.getResult();

	}

	private void setAdditionalInfoWithDocumentDate(CustomerDocumentInfo customerDocumentInfo) {
		List<CustomerDocument> customerDocuments = customerDocumentInfo.getCustomerDocuments();
		// Get the current date and set in the document additional info as
		// the document timestamp
		Calendar now = Calendar.getInstance();
		String formattedDate = dateFormatter.format(now.getTime());
		// set the document date in additional info.
		if (CollectionUtils.isNotEmpty(customerDocuments)) {
			for (CustomerDocument customerDocument : customerDocuments) {
				if (customerDocument != null) {
					String documentAdditionalInfo = customerDocument.getDocumentAdditionalInfo();
					if (VALID_STRING.equals(documentAdditionalInfo)) {
						customerDocument.setDocumentAdditionalInfo(formattedDate);
					} else {
						customerDocument.setDocumentAdditionalInfo(documentAdditionalInfo);
					}
				}
			}
		}
	}

}
