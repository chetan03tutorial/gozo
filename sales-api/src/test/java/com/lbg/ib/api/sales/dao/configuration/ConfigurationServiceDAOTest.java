package com.lbg.ib.api.sales.dao.configuration;

public class ConfigurationServiceDAOTest {

    /*
     * @InjectMocks private ConfigurationServiceDAO configDao;
     * 
     * @Mock private ConfigurationService configService;
     * 
     * private Map<String, Object> configurationMap;
     */

    /*
     * private Map<String, Object> prepareConfigurationMap() { configurationMap
     * = new HashMap<String, Object>(); Map<String, Object> properties = new
     * HashMap<String, Object>(); properties.put("ADAPA_HEADERS",
     * "ADAPA_HEADERS"); configurationMap.put("APTDA", properties); return
     * configurationMap; }
     */

    /*
     * @Before public void setup() { prepareConfigurationMap();
     * 
     * when(configService.getConfigurationItems(anyString())).thenReturn(
     * configurationMap); when(configService.getConfigurationValue(anyString(),
     * anyString())).thenReturn("StringValue"); }
     */
    /*
     * @Test public void testConfigurationService() throws NoSuchFieldException,
     * SecurityException, Exception {
     * 
     * setFinalStatic(ConfigurationServiceDAO.class.getDeclaredField(
     * "CONFIGURATION_SERVICE"), configService);
     * 
     * assertNotNull(configDao.getConfigurationItems("a"));
     * assertNotNull(configDao.getConfigurationValue("a", "b"));
     * assertNotNull(configDao.getConfigurationStringValue("a", "b")); }
     */

    /*
     * @Test public void testConfigurationServiceForNullValue() throws
     * NoSuchFieldException, SecurityException, Exception {
     * when(configService.getConfigurationValue(anyString(),
     * anyString())).thenReturn(null);
     * setFinalStatic(ConfigurationServiceDAO.class.getDeclaredField(
     * "CONFIGURATION_SERVICE"), configService);
     * assertNull(configDao.getConfigurationStringValue("a", "b"));
     * 
     * }
     */
    /*
     * static void setFinalStatic(Field field, Object newValue) throws Exception
     * { field.setAccessible(true); Field modifiersField =
     * Field.class.getDeclaredField("modifiers");
     * modifiersField.setAccessible(true); modifiersField.setInt(field,
     * field.getModifiers() & ~Modifier.FINAL); field.set(null, newValue); }
     */
}
