package com.lbg.ib.api.sales.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.lbg.ib.api.sales.sortcode.resource.SortCodeMapper;
import com.lbg.ib.api.sales.sortcode.service.SortCodeCacheServiceImpl;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.enums.BrandValue;


@Component
public class ActivateSortCodeServiceImpl implements ActivateSortCodeService {

	@Autowired
	private ChannelBrandingDAO channelBrandingService;
	@Autowired
	private LoggerDAO logger;
	private static final String BACKSPACE = "\\b";
	private Map<String, SortCodeMapper> loadMaperBybranch(String brand) {
		Map<String, SortCodeMapper> branchSortCodemaper = null;
		if ((BrandValue.LLOYDS.getBrand()).equalsIgnoreCase(brand)) {
			branchSortCodemaper = SortCodeCacheServiceImpl.getLloydsSortCodemaper();
		}
		if ((BrandValue.HALIFAX.getBrand()).equalsIgnoreCase(brand)) {
			branchSortCodemaper = SortCodeCacheServiceImpl.getHalifaxSortCodemaper();
		}
		if ((BrandValue.BOS.getBrand()).equalsIgnoreCase(brand)) {
			branchSortCodemaper = SortCodeCacheServiceImpl.getBosSortCodemaper();
		}
		return branchSortCodemaper;
	}

	private String defultBranchByBrand(String brand) {
		String branchName = null;
		List<String> defaultSortCodes = null;
		if ((BrandValue.LLOYDS.getBrand()).equalsIgnoreCase(brand)) {
			defaultSortCodes = SortCodeCacheServiceImpl.getLloydsSortCodemaperDefault();
		}
		if ((BrandValue.HALIFAX.getBrand()).equalsIgnoreCase(brand)) {
			defaultSortCodes = SortCodeCacheServiceImpl.getHalifaxSortCodemaperDefault();
		}
		if ((BrandValue.BOS.getBrand()).equalsIgnoreCase(brand)) {
			defaultSortCodes = SortCodeCacheServiceImpl.getBosSortCodemaperDefault();
		}
		if (!CollectionUtils.isEmpty(defaultSortCodes)) {
			int indexPosition = generateRandomInt(defaultSortCodes.size());
			branchName = defaultSortCodes.get(indexPosition);
		}
		return branchName;
	}

	private static int generateRandomInt(int upperRange) {
		Random random = new Random();
		return random.nextInt(upperRange);
	}

	public String getBrand() {
		DAOResponse<ChannelBrandDTO> channel = channelBrandingService.getChannelBrand();
		String brand =null;
		if(channel!=null) {
			 brand = channel.getResult().getBrand();
		}
		return brand;
	}

	public String getSortCodeByTown(String brand, String townName) {
		logger.traceLog(this.getClass(), ":::Going to fetch sort code as not received by location for brand: "+brand+": town:"+townName);
		String sortCode = null;
		try {
			if(!StringUtils.isEmpty(brand) && !StringUtils.isEmpty(townName)) {
				Map<String, SortCodeMapper> branchSortCodemaper = loadMaperBybranch(brand);
				SortCodeMapper sortCodeMaperBean = null;
				if (branchSortCodemaper != null && branchSortCodemaper.size() > 0) {
					sortCodeMaperBean = branchSortCodemaper.get(townName);
					if (sortCodeMaperBean != null) {
						logger.traceLog(this.getClass(), ":::Found branch by town:::"+townName +" is "+sortCodeMaperBean.getSortCode());
						sortCode = sortCodeMaperBean.getSortCode();
					}else {
						List<String> nearestSortCodesByTown = getNearestSortCodesByTown(branchSortCodemaper, townName);
						if (nearestSortCodesByTown != null && nearestSortCodesByTown.size() > 0) {
							logger.traceLog(this.getClass(), ":::Received total sort code for :::"+townName +" is "+nearestSortCodesByTown.size());
							int randomDefaultIndex = generateRandomInt(nearestSortCodesByTown.size());
							sortCode = nearestSortCodesByTown.get(randomDefaultIndex);
							logger.traceLog(this.getClass(), ":::Found nearest branch by town:::"+townName +" is "+sortCode);
						}else {
							sortCode = defultBranchByBrand(brand);
							logger.traceLog(this.getClass(), ":::Setting defult sort code for:::"+townName +" is "+sortCode);
						}
					}
				}
			}
		}catch (Exception x) {
			logger.logException(this.getClass(), x);
		}
		logger.traceLog(this.getClass(), ":::Setting sort code for :::"+townName +" is "+sortCode);
		return sortCode;
	}


	private List<String> getNearestSortCodesByTown(Map<String, SortCodeMapper> branchSortCodemaper, String townName) {
		List<String> nearestSortCodeList = new ArrayList<String>();
		for (Map.Entry<String, SortCodeMapper> entry : branchSortCodemaper.entrySet()) {
			if (isContain(entry.getKey(),townName)) {
				logger.traceLog(this.getClass(), ":::Adding town:::"+entry.getKey() +" into list is "+entry.getValue().getSortCode());
				nearestSortCodeList.add(entry.getValue().getSortCode());
			}
		}
		return nearestSortCodeList;
	}
	
	private boolean isContain(String source, String subItem) {
		String pattern = BACKSPACE + subItem.toLowerCase() + BACKSPACE;
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source.toLowerCase());
		return m.find();
	}

}
