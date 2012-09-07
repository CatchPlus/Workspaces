package nl.of.catchplus

class Setting extends BaseDomain
{
    static final String _COMPASS_INDEX_LOCATION = "COMPASS_INDEX_LOCATION"


    String key
    String value

    String beschrijving

    /*SettingGroup groep = SettingGroup.REMOTE_URL
    SettingType type = SettingType.URL*/

    /*static VIEW_LIST = ['id', 'key', 'value', 'type', 'groep']*/

    static constraints = {
        key(nullable: false, blank: false, maxSize: 255)
        value(nullable: false, blank: false, maxSize: 255)
        /*type(nullable: false)
        groep(nullable: false)*/
        beschrijving(nullable: true, blank: true, maxSize: 255)
    }

    static String stringValue(String _key)
        {
            Setting aSetting = Setting.findByKey(_key)

            return aSetting.value.toString()
        }
}


    /*static String cmscHomeUrlToString()
    {
        return Setting.findByKey('REMOTE_URL_PREFIX')?.value
    }

    static double btwPercentage()
    {
        return Double.parseDouble(Setting.findByKey(Setting._BTW_PERCENTAGE).value)
    }

    static boolean booleanValue(String _key)
    {
        Setting aSetting = Setting.findByKey(_key)

        Boolean aBoolean = Boolean.valueOf(aSetting.value)

        return aBoolean.booleanValue()
    }

    static double doubleValue(String _key)
    {
        Setting aSetting = Setting.findByKey(_key)

        Double aDouble = Double.valueOf(aSetting.value)

        return aDouble.doubleValue()
    }
    static int integerValue(String _key)
    {
        Setting aSetting = Setting.findByKey(_key)

        Integer aInteger = Integer.valueOf(aSetting.value)

        return aInteger.intValue()
    }

    static String stringValue(String _key)
    {
        Setting aSetting = Setting.findByKey(_key)

        return aSetting.value.toString()
    }


    //TODO test if null and test if type:BOOL
}


enum SettingType {
    STRING, BOOL, URL, EMAIL, NUMBER, CURRENCY, PERCENTAGE
}

enum SettingGroup {
    SYSTEM, REMOTE_URL, OTHER, DOMAIN
}*/