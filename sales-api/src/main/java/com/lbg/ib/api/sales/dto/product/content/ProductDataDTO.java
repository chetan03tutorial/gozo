package com.lbg.ib.api.sales.dto.product.content;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductDataDTO {

    private String                title;
    private String                productMnemonic;
    private String                productSummary;
    private String                productOverviewSummary;
    private Integer               productRank;
    private String                applicationLink;
    private String                additionalInfomationLink;
    private Image                 image;
    private Boolean               isAvailableOnline;
    private String                offlineProductSummary;
    private String                featuresAndBenefits;

    private String                keepInMind;
    private List<ProductCategory> productCategories = new ArrayList<ProductCategory>();
    private Promotion             promotion;
    private SegmentedPricing      segmentedPricing;
    private List<Feature>         features          = new ArrayList<Feature>();

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The productMnemonic
     */
    public String getProductMnemonic() {
        return productMnemonic;
    }

    /**
     * @param productMnemonic
     *            The productMnemonic
     */
    public void setProductMnemonic(String productMnemonic) {
        this.productMnemonic = productMnemonic;
    }

    /**
     * @return The productSummary
     */
    public String getProductSummary() {
        return productSummary;
    }

    /**
     * @param productSummary
     *            The productSummary
     */
    public void setProductSummary(String productSummary) {
        this.productSummary = productSummary;
    }

    /**
     * @return The productOverviewSummary
     */
    public String getProductOverviewSummary() {
        return productOverviewSummary;
    }

    /**
     * @param productOverviewSummary
     *            The productOverviewSummary
     */
    public void setProductOverviewSummary(String productOverviewSummary) {
        this.productOverviewSummary = productOverviewSummary;
    }

    /**
     * @return The productRank
     */
    public Integer getProductRank() {
        return productRank;
    }

    /**
     * @param productRank
     *            The productRank
     */
    public void setProductRank(Integer productRank) {
        this.productRank = productRank;
    }

    /**
     * @return The applicationLink
     */
    public String getApplicationLink() {
        return applicationLink;
    }

    /**
     * @param applicationLink
     *            The applicationLink
     */
    public void setApplicationLink(String applicationLink) {
        this.applicationLink = applicationLink;
    }

    /**
     * @return The additionalInfomationLink
     */
    public String getAdditionalInfomationLink() {
        return additionalInfomationLink;
    }

    /**
     * @param additionalInfomationLink
     *            The additionalInfomationLink
     */
    public void setAdditionalInfomationLink(String additionalInfomationLink) {
        this.additionalInfomationLink = additionalInfomationLink;
    }

    /**
     * @return The image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image
     *            The image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return The isAvailableOnline
     */
    public Boolean getIsAvailableOnline() {
        return isAvailableOnline;
    }

    /**
     * @param isAvailableOnline
     *            The isAvailableOnline
     */
    public void setIsAvailableOnline(Boolean isAvailableOnline) {
        this.isAvailableOnline = isAvailableOnline;
    }

    /**
     * @return The offlineProductSummary
     */
    public String getOfflineProductSummary() {
        return offlineProductSummary;
    }

    /**
     * @param offlineProductSummary
     *            The offlineProductSummary
     */
    public void setOfflineProductSummary(String offlineProductSummary) {
        this.offlineProductSummary = offlineProductSummary;
    }

    /**
     * @return The featuresAndBenefits
     */
    public String getFeaturesAndBenefits() {
        return featuresAndBenefits;
    }

    /**
     * @param featuresAndBenefits
     *            The featuresAndBenefits
     */
    public void setFeaturesAndBenefits(String featuresAndBenefits) {
        this.featuresAndBenefits = featuresAndBenefits;
    }

    /**
     * @return The keepInMind
     */
    public String getKeepInMind() {
        return keepInMind;
    }

    /**
     * @param keepInMind
     *            The keepInMind
     */
    public void setKeepInMind(String keepInMind) {
        this.keepInMind = keepInMind;
    }

    /**
     * @return The productCategories
     */
    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    /**
     * @param productCategories
     *            The productCategories
     */
    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    /**
     * @return The promotion
     */
    public Promotion getPromotion() {
        return promotion;
    }

    /**
     * @param promotion
     *            The promotion
     */
    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    /**
     * @return The segmentedPricing
     */
    public SegmentedPricing getSegmentedPricing() {
        return segmentedPricing;
    }

    /**
     * @param segmentedPricing
     *            The segmentedPricing
     */
    public void setSegmentedPricing(SegmentedPricing segmentedPricing) {
        this.segmentedPricing = segmentedPricing;
    }

    /**
     * @return The features
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     * @param features
     *            The features
     */
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

}

class Feature {

    private String  id;
    private String  labelShort;
    private String  labelLong;
    private String  value;
    private String  briefDescription;
    private String  expandedDescription;
    private String  detail;
    private Boolean isVisibleOnComparisonTable;
    private Boolean isVisibleOnProductDetail;
    private String  productDetailLinkOnComparisonTable;
    private String  productSummary;
    private String  featureRank;
    private String  tooltipTitle;
    private String  tooltipContent;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The labelShort
     */
    public String getLabelShort() {
        return labelShort;
    }

    /**
     * @param labelShort
     *            The labelShort
     */
    public void setLabelShort(String labelShort) {
        this.labelShort = labelShort;
    }

    /**
     * @return The labelLong
     */
    public String getLabelLong() {
        return labelLong;
    }

    /**
     * @param labelLong
     *            The labelLong
     */
    public void setLabelLong(String labelLong) {
        this.labelLong = labelLong;
    }

    /**
     * @return The value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return The briefDescription
     */
    public String getBriefDescription() {
        return briefDescription;
    }

    /**
     * @param briefDescription
     *            The briefDescription
     */
    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    /**
     * @return The expandedDescription
     */
    public String getExpandedDescription() {
        return expandedDescription;
    }

    /**
     * @param expandedDescription
     *            The expandedDescription
     */
    public void setExpandedDescription(String expandedDescription) {
        this.expandedDescription = expandedDescription;
    }

    /**
     * @return The detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail
     *            The detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @return The isVisibleOnComparisonTable
     */
    public Boolean getIsVisibleOnComparisonTable() {
        return isVisibleOnComparisonTable;
    }

    /**
     * @param isVisibleOnComparisonTable
     *            The isVisibleOnComparisonTable
     */
    public void setIsVisibleOnComparisonTable(Boolean isVisibleOnComparisonTable) {
        this.isVisibleOnComparisonTable = isVisibleOnComparisonTable;
    }

    /**
     * @return The isVisibleOnProductDetail
     */
    public Boolean getIsVisibleOnProductDetail() {
        return isVisibleOnProductDetail;
    }

    /**
     * @param isVisibleOnProductDetail
     *            The isVisibleOnProductDetail
     */
    public void setIsVisibleOnProductDetail(Boolean isVisibleOnProductDetail) {
        this.isVisibleOnProductDetail = isVisibleOnProductDetail;
    }

    /**
     * @return The productDetailLinkOnComparisonTable
     */
    public String getProductDetailLinkOnComparisonTable() {
        return productDetailLinkOnComparisonTable;
    }

    /**
     * @param productDetailLinkOnComparisonTable
     *            The productDetailLinkOnComparisonTable
     */
    public void setProductDetailLinkOnComparisonTable(String productDetailLinkOnComparisonTable) {
        this.productDetailLinkOnComparisonTable = productDetailLinkOnComparisonTable;
    }

    /**
     * @return The productSummary
     */
    public String getProductSummary() {
        return productSummary;
    }

    /**
     * @param productSummary
     *            The productSummary
     */
    public void setProductSummary(String productSummary) {
        this.productSummary = productSummary;
    }

    /**
     * @return The featureRank
     */
    public String getFeatureRank() {
        return featureRank;
    }

    /**
     * @param featureRank
     *            The featureRank
     */
    public void setFeatureRank(String featureRank) {
        this.featureRank = featureRank;
    }

    /**
     * @return The tooltipTitle
     */
    public String getTooltipTitle() {
        return tooltipTitle;
    }

    /**
     * @param tooltipTitle
     *            The tooltipTitle
     */
    public void setTooltipTitle(String tooltipTitle) {
        this.tooltipTitle = tooltipTitle;
    }

    /**
     * @return The tooltipContent
     */
    public String getTooltipContent() {
        return tooltipContent;
    }

    /**
     * @param tooltipContent
     *            The tooltipContent
     */
    public void setTooltipContent(String tooltipContent) {
        this.tooltipContent = tooltipContent;
    }

}

class Image {

    @JsonProperty("default")
    private String defaultValue;
    private String thumbnail;
    private String highResolution;
    private String hero;

    /**
     * @return The defaultValue
     */
    @JsonProperty("default")
    public String getDefault() {
        return defaultValue;
    }

    /**
     * @param _default
     *            The default
     */
    @JsonProperty("default")
    public void setDefault(String defaultString) {
        this.defaultValue = defaultString;
    }

    /**
     * @return The thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail
     *            The thumbnail
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return The highResolution
     */
    public String getHighResolution() {
        return highResolution;
    }

    /**
     * @param highResolution
     *            The highResolution
     */
    public void setHighResolution(String highResolution) {
        this.highResolution = highResolution;
    }

    /**
     * @return The hero
     */
    public String getHero() {
        return hero;
    }

    /**
     * @param hero
     *            The hero
     */
    public void setHero(String hero) {
        this.hero = hero;
    }

}

class ProductCategory {

    private String key;
    private String value;

    /**
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     *            The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return The value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            The value
     */
    public void setValue(String value) {
        this.value = value;
    }

}

class Promotion {

    private String bannerTitle;
    private String bannerContent;
    private String stickerText;
    private String offerDetailTitle;
    private String offerDetailText;
    private String productDetailTitle;
    private String productOfferSummary;
    private String productOfferDetail;

    /**
     * @return The bannerTitle
     */
    public String getBannerTitle() {
        return bannerTitle;
    }

    /**
     * @param bannerTitle
     *            The bannerTitle
     */
    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    /**
     * @return The bannerContent
     */
    public String getBannerContent() {
        return bannerContent;
    }

    /**
     * @param bannerContent
     *            The bannerContent
     */
    public void setBannerContent(String bannerContent) {
        this.bannerContent = bannerContent;
    }

    /**
     * @return The stickerText
     */
    public String getStickerText() {
        return stickerText;
    }

    /**
     * @param stickerText
     *            The stickerText
     */
    public void setStickerText(String stickerText) {
        this.stickerText = stickerText;
    }

    /**
     * @return The offerDetailTitle
     */
    public String getOfferDetailTitle() {
        return offerDetailTitle;
    }

    /**
     * @param offerDetailTitle
     *            The offerDetailTitle
     */
    public void setOfferDetailTitle(String offerDetailTitle) {
        this.offerDetailTitle = offerDetailTitle;
    }

    /**
     * @return The offerDetailText
     */
    public String getOfferDetailText() {
        return offerDetailText;
    }

    /**
     * @param offerDetailText
     *            The offerDetailText
     */
    public void setOfferDetailText(String offerDetailText) {
        this.offerDetailText = offerDetailText;
    }

    /**
     * @return The productDetailTitle
     */
    public String getProductDetailTitle() {
        return productDetailTitle;
    }

    /**
     * @param productDetailTitle
     *            The productDetailTitle
     */
    public void setProductDetailTitle(String productDetailTitle) {
        this.productDetailTitle = productDetailTitle;
    }

    /**
     * @return The productOfferSummary
     */
    public String getProductOfferSummary() {
        return productOfferSummary;
    }

    /**
     * @param productOfferSummary
     *            The productOfferSummary
     */
    public void setProductOfferSummary(String productOfferSummary) {
        this.productOfferSummary = productOfferSummary;
    }

    /**
     * @return The productOfferDetail
     */
    public String getProductOfferDetail() {
        return productOfferDetail;
    }

    /**
     * @param productOfferDetail
     *            The productOfferDetail
     */
    public void setProductOfferDetail(String productOfferDetail) {
        this.productOfferDetail = productOfferDetail;
    }

}

class SegmentedPricing {

    private String grossRate;
    private String introductoryBonus;
    private String preferentialRate;

    /**
     * @return The grossRate
     */
    public String getGrossRate() {
        return grossRate;
    }

    /**
     * @param grossRate
     *            The grossRate
     */
    public void setGrossRate(String grossRate) {
        this.grossRate = grossRate;
    }

    /**
     * @return The introductoryBonus
     */
    public String getIntroductoryBonus() {
        return introductoryBonus;
    }

    /**
     * @param introductoryBonus
     *            The introductoryBonus
     */
    public void setIntroductoryBonus(String introductoryBonus) {
        this.introductoryBonus = introductoryBonus;
    }

    /**
     * @return The preferentialRate
     */
    public String getPreferentialRate() {
        return preferentialRate;
    }

    /**
     * @param preferentialRate
     *            The preferentialRate
     */
    public void setPreferentialRate(String preferentialRate) {
        this.preferentialRate = preferentialRate;
    }

}