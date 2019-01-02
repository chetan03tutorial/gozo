/**********************************************************************

 * This source code is the property of Lloyd Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.sso.domain.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.product.holding.domain.Arrangement;
import com.lbg.ib.api.sales.dao.product.holding.domain.ArrangementAdditionalDetails;
import com.lbg.ib.api.sales.dao.product.holding.domain.Customer;
import com.lbg.ib.api.sales.dao.product.holding.domain.ProductHolding;
import com.lbg.ib.api.sso.domain.product.Event;
import com.lbg.ib.api.sso.domain.product.Indicator;
import com.lbg.ib.api.sales.product.domain.Product;
import com.lbg.ib.api.sales.user.service.ProductHoldingComparator;

@Component
public class ProductHoldingResponseMapper {

    @Autowired
    private ConfigurationDAO   configuration;

    @Autowired
    private LoggerDAO          logger;

    public static final String DORMANT                       = "Dormant";

    public static final String PRODUCT_HOLDING_DISPLAY_ORDER = "ProductHoldiDisplayOrder";

    public static final String DATE_FORMAT                   = "E MMM dd HH:mm:ss Z yyyy";

    public static final String INDICATORS_FOR_KYC_REFRESH    = "IndicatorsForKYCRefresh";

    @TraceLog
    public com.lbg.ib.api.sales.product.domain.ProductHolding populateProductHoldingResponse(ProductHolding response) {
        com.lbg.ib.api.sales.product.domain.ProductHolding productHolding = new com.lbg.ib.api.sales.product.domain.ProductHolding();
        List<Arrangement> arrangements = response.getArrangements();
        Customer customer = response.getCustomer();
		List<Product> products = mapArrangements(arrangements, customer);
        List<Product> productsData = new ArrayList<Product>();
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                productsData.add(product);
            }
        }
        Collections.sort(productsData, new ProductHoldingComparator());
        productHolding.setProducts(productsData);
        productHolding.setOcisId(customer.getOcisId());
        productHolding.setCbsCustomerNumber(customer.getCbsCustomerNum());
        setKycDetails(response, productHolding, arrangements);
        return productHolding;
    }

	/**
	 * @param response
	 * @param productHolding
	 * @param arrangements
	 */
	private void setKycDetails(ProductHolding response,
			com.lbg.ib.api.sales.product.domain.ProductHolding productHolding,
			List<Arrangement> arrangements) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        if (null != response.getCustomer().getLastLoginDate()) {
            try {
                dateFormat.parse(response.getCustomer().getLastLoginDate());
                Calendar cal = dateFormat.getCalendar();
                productHolding.setLastLoginDate(cal);
            } catch (ParseException e) {
                logger.logException(this.getClass(), e);
            }
        }

        productHolding.setInternalUserIdentifier(response.getCustomer().getInternalUserIdentifier());
        if (null != response.getCustomer() && null != response.getCustomer().getCategories()) {
            productHolding.setCategories(mapCategories(response.getCustomer().getCategories()));
        }

        productHolding.setKycRefreshRequired(kycIndicatorCheck(arrangements));
        if (!productHolding.getKycRefreshRequired() && (null == arrangements || arrangements.isEmpty())) {
            productHolding.setKycRefreshRequired(true);
        }
	}

    @TraceLog
    private List<Product> mapArrangements(List<Arrangement> arrangements, Customer customer) {
        List<Product> products = new ArrayList<Product>();
        for (Arrangement arrangement : arrangements) {
            Product product = new Product();
            product.setAccountName(arrangement.getAccountName());
            product.setAccountNumber(arrangement.getAccountNumber());
            product.setExternalSystemId(arrangement.getExternalSystemId());
            product.setSortCode(arrangement.getSortCode());
            product.setSellingLegalEntity(arrangement.getSellingLegalEntity());
            product.setManufacturingLegalEntity(arrangement.getManufacturingLegalEntity());
            if (null != customer && null != customer.getHost() && null != arrangement.getAccountType()) {
                product.setProductIdentifier(arrangement.getAccountType().replace(customer.getHost(), ""));
            } else {
                product.setProductIdentifier(arrangement.getAccountType());
            }
            product.setAccountType(arrangement.getAccountType());
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            if (null != arrangement.getAccountOpenedDate()) {
                try {
                    dateFormat.parse(arrangement.getAccountOpenedDate());
                    Calendar cal = dateFormat.getCalendar();
                    product.setAccountOpenedDate(cal);
                } catch (ParseException e) {
                    logger.logException(this.getClass(), e);
                }
            }

            product = populateAdditionalInformation(product, arrangement.getArrangementDetail());

            mapArrangementforAccountStatus(products, arrangement, product);
        }

        return products;
    }

	/**
	 * @param products
	 * @param arrangement
	 * @param product
	 */
	private void mapArrangementforAccountStatus(List<Product> products,
			Arrangement arrangement, Product product) {
		if (null != arrangement.getProductArrangementLifecycleStatus()) {
		    product.setAccountStatus(arrangement.getProductArrangementLifecycleStatus().getValue());
		}

		if (null != arrangement.getEvents() && !arrangement.getEvents().isEmpty()) {
		    List<Event> events = new ArrayList<Event>();
		    for (com.lbg.ib.api.sso.domain.product.Event event : arrangement.getEvents()) {
		        Event productEvent = new Event();
		        productEvent.setEventId(event.getEventId());
		        events.add(productEvent);
		    }
		    product.setEvents(events);
		}
		if (null != product.getDisplayOrder()) {
		    products.add(product);
		}
	}

    private List<com.lbg.ib.api.sso.domain.product.Category> mapCategories(List<Category> categories) {
        List<com.lbg.ib.api.sso.domain.product.Category> categoryList = new ArrayList<com.lbg.ib.api.sso.domain.product.Category>();
        for (Category category : categories) {
            com.lbg.ib.api.sso.domain.product.Category newCategory = new com.lbg.ib.api.sso.domain.product.Category();
            newCategory.setCategoryId(category.getCategoryId());
            categoryList.add(newCategory);
        }
        return categoryList;
    }

    private Product populateAdditionalInformation(Product product, ArrangementAdditionalDetails additionalDetails) {
        if (null != additionalDetails) {
            product.setAccountNickName(additionalDetails.getName());
            product.setAceptTransfers(additionalDetails.getAcceptTransfers());
            product.setAvailableBal(additionalDetails.getAmtAvailableBal());
            product.setCurrencyCode(additionalDetails.getCurrencyCode());
            product.setLedgerBal(additionalDetails.getAmtLedgerBal());
            product.setOverdraftLimit(additionalDetails.getAmtOverdraftLimit());
            product.setBalanceInfoIsValid(additionalDetails.getBalanceInfoIsValid());
            product.setLifecyclestatus(additionalDetails.getLifecycleStatus());
            if (null != additionalDetails.getAccGroupType()) {
                product.setProductType(additionalDetails.getAccGroupType());
            }
            if (DORMANT.equalsIgnoreCase(additionalDetails.getLifecycleStatus())) {
                product.setDormant(true);
            } else {
                product.setDormant(false);
            }
            if (null != additionalDetails.getIndicators() && !additionalDetails.getIndicators().isEmpty()) {
                product.setIndicators(mapIndicators(additionalDetails.getIndicators()));
            }
            Map<String, Object> displayOrder = configuration.getConfigurationItems(PRODUCT_HOLDING_DISPLAY_ORDER);
            if (null != additionalDetails.getAccGroupType() && null != additionalDetails.getAccGroupType()) {
                String order = (String) displayOrder.get(additionalDetails.getAccGroupType().toUpperCase());
                if (null != order) {
                    product.setDisplayOrder(Integer.parseInt(order));
                }
            }
        }

        return product;
    }

    private List<Indicator> mapIndicators(List<com.lbg.ib.api.sso.domain.product.Indicator> indicators) {
        List<Indicator> newIndicators = new ArrayList<Indicator>();
        for (com.lbg.ib.api.sso.domain.product.Indicator indicator : indicators) {
            Indicator productIndicator = new Indicator();
            productIndicator.setIndicatorCode(indicator.getIndicatorCode());
            newIndicators.add(productIndicator);
        }
        return newIndicators;
    }

    public void setLoggerDAO(LoggerDAO logger) {
        this.logger = logger;
    }

    public void setConfiguration(ConfigurationDAO configuration) {
        this.configuration = configuration;
    }

    // Setting the KYC indicator boolean
    private boolean kycIndicatorCheck(List<Arrangement> arrangements) {
        Map<String, Object> map = configuration.getConfigurationItems(INDICATORS_FOR_KYC_REFRESH);
        if (null != arrangements) {
            for (Arrangement arrangement : arrangements) {
                if (null != arrangement.getArrangementDetail()
                        && null != arrangement.getArrangementDetail().getIndicators()
                        && arrangement.getArrangementDetail().getIndicators().size() > 0) {
                    for (com.lbg.ib.api.sso.domain.product.Indicator indicator : arrangement
                            .getArrangementDetail().getIndicators()) {
                        if (map.containsValue(indicator.getIndicatorCode())) {
                            logger.traceLog(this.getClass(), "Setting the KYC indicator.");
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }
}