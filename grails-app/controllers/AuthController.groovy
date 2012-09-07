import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken

import org.apache.shiro.web.util.WebUtils
import nl.of.catchplus.ShiroService
import nl.of.catchplus.ShiroUser
import nl.of.catchplus.Account
import nl.of.catchplus.Bundle
import nl.of.catchplus.BaseContent
import nl.of.catchplus.StoredFile
import nl.of.catchplus.Institution
import nl.of.catchplus.Collection
//import groovyx.net.http.HTTPBuilder
import nl.of.catchplus.WorkSpace
import nl.of.catchplus.ShiroRole

import org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin

import org.springframework.web.context.request.RequestContextHolder;


import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext
import nl.of.catchplus.BaseDomain
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.HttpResponse
import org.apache.http.HttpEntity
import org.apache.http.util.EntityUtils

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.apache.shiro.authc.NotActivatedAccountException;

class AuthController implements ApplicationContextAware
{
    def shiroSecurityManager

    ApplicationContext applicationContext

    ShiroService shiroService

    def index = { redirect(action: "login", params: params) }

    def searchableService

    def testService

    def sessionFactory

    def gormService

    def emailService

    def httpSessionService

    def collection = {

        searchableService.stopMirroring()

        def workSpace = WorkSpace.list().iterator().next()

        def institutionID = shiroService.getActiveShiroUser(true).institution.id


        def userId = shiroService.getActiveShiroUser(false).id




        for (int k = 0; k < 1; k++)
        {

            Collection collection
            Institution institution
            def user = ShiroUser.get(userId)

            Collection.withTransaction {

                institution = Institution.get(institutionID)
                user = ShiroUser.get(userId)

                collection = new Collection(createdBy: user, title: "collection" + k, ownerWorkSpace: workSpace, description: "set beschrijving " + k, owner: institution).save()
                workSpace.addToCollection(collection)

                //set admin
                ShiroRole collectionAdminRole = testService.createCollectionAdminRole(collection)
                user.addToRoles(collectionAdminRole)

                testService.createCollectionUserAddRole(collection)
                ShiroRole userReadRole = testService.createCollectionUserReadRole(collection)

                institution.group.addToRoles(userReadRole)


            }

            def time = System.currentTimeMillis()

            for (int l = 0; l < 10; l++)
            {
                def http = null//new HTTPBuilder('http://localhost:8080/catchplus/auth/bundle')
                def postBody = [collection: collection.id, user: user.id] // will be url-encoded


                http.post(body: postBody/*, requestContentType: URLENC*/) { resp ->
                    //   println 'end bundle create HTTP'
                    //assert resp.statusLine.statusCode == 302
                    //println 'stautscode: ' + resp.statusLine.statusCode
                }

                http = null
                postBody = null

                def r = RequestContextHolder.getRequestAttributes()
                println r

                println 'bundle ' + l + " : " + (System.currentTimeMillis() - time)
                time = System.currentTimeMillis()

                println 'A'
                org.hibernate.classic.Session sessionn = sessionFactory.getCurrentSession()
                sessionn.clear()
                println 'B'
                sessionn.flush()
                DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP.get().clear()
                println 'C'

                //RequestContextHolder.resetRequestAttributes()
                //RequestContextListener.resetRequestAttributes()
            }

            //


        }
    }

    def bundle = {

        /*   println 'bundle'
                println params.collection
                println params.user
        */
        WorkSpace workSpace = WorkSpace.list().iterator().next()
        Collection collection = Collection.get(params.collection)
        Bundle bundle
        def user = ShiroUser.get(params.user)
        Institution institution = user.institution



        Bundle.withTransaction {


            bundle = new Bundle(createdBy: user, ownerCollection: collection, title: "bundle titel" /*+ "" + k + "" + l*/, description: " bundle beschrijving" /*+ k + "" + l*/, owner: institution).save()
            collection.addToBundle(bundle)


        }

        /*workSpace.bundleMetaRecordKey.each {key ->
            MetaRecord metaRecord
                metaRecord = new MetaRecord(key: key, value: 'avalue')
                metaRecord.save()
                bundle.addToMetaRecord(metaRecord)

        }*/


        for (int m = 0; m < 1; m++)
        {
            def http = null//new HTTPBuilder('http://localhost:8080/catchplus/auth/content')
            def postBody = [bundle: bundle.id, user: user.id] // will be url-encoded


            http.post(body: postBody/*, requestContentType: URLENC*/) { resp ->
                // println 'end content create HTTP'
                //assert resp.statusLine.statusCode == 302
                //println 'stautscode2: ' + resp.statusLine.statusCode
            }
            http = null
            postBody = null
        }
        bundle = null



        render 'endddd'


    }

    def test = {
        gormService.indexAll()
    }

    def content = {


        def user = ShiroUser.get(params.user)

        Institution institution = user.institution

        WorkSpace workSpace = WorkSpace.list().iterator().next()


        Bundle bundle = Bundle.get(params.bundle)
        for (int m = 0; m < 10; m++)
        {
            BaseContent content
            StoredFile storedFile



            BaseContent.withTransaction {


                content = new BaseContent(createdBy: user, title: "content titel" /*+ k + "" + l + ""*/ + m, description: " content beschrijving" /*+ k + "" + l + ""*/ + m, url: """www.content-url${ /*k + "" + l + ""*/ +m}.nl""", ownerBundle: bundle, owner: institution).save()
                bundle.addToContent(content)

                /*workSpace.baseContentMetaRecordKey.each {key ->
                    MetaRecord metaRecord

                    metaRecord = new MetaRecord(key: key, value: 'avalue')
                    metaRecord.save()
                    content.addToMetaRecord(metaRecord)
                }*/



                storedFile = new StoredFile(contentType: 'txt', contentLength: 1024, originalFilename: 'dummyfile', md5: 'testmd5', content: content)
                storedFile.save()
                content.storedFile = storedFile

                // println 'content ' + m

                content.save()
/*
            }


            workSpace.baseContentMetaRecordKey.each {key ->
                MetaRecord metaRecord
                MetaRecord.withTransaction {

                    metaRecord = new MetaRecord(key: key, value: 'avalue')
                    metaRecord.save()
                    content.addToMetaRecord(metaRecord)
                }
                metaRecord = null*/


            }
            content = null
            storedFile = null

        }


        render 'endddd'


    }

    def clearDB = {
            (ApplicationHolder.application.getArtefacts("Domain") as List).each {
              it.newInstance().list()*.delete()
            }
            sessionFactory.currentSession.flush()
            sessionFactory.currentSession.clear()
          }

    def upload2 =
        {
            System.out.println('Start upload REST ');



            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost('http://localhost:8080/catchplus/api/baseContent');

            String ss = "105, 1106, 1217, 1219, 1221, 1223, 1225, 1227, 1229, 1231, 1233, 1235, 1237, 1239, 1241, 1243, 1245, 1247, 1249, 1251, 1253, 1255, 1257, 1259, 1261, 1263, 1265, 1267, 1269, 1271, 1273, 1275, 1277, 1279, 1281, 1283, 1285, 1287, 1289, 1291, 1293, 1295, 1297, 1299, 1301, 1303, 1305, 1307, 1309, 1311, 1313, 1315, 1317, 1319, 1321, 1323, 1325, 1327, 1329, 1331, 1333, 1335, 1337, 1339, 1341, 1343, 1345, 1347, 1349, 1351, 1353, 1355, 1357, 1359, 1361, 1363, 1365, 1367, 1369, 1371, 1373, 1375, 1377, 1379, 1381, 1383, 1385, 1387, 1389, 1391, 1393, 1395, 1397, 1399, 1401, 1403, 1405, 1407, 1409, 1411, 1413, 1415, 1417, 1419, 1421, 1423, 1425, 1427, 1429, 1431, 1433, 1435, 1437, 1439, 1441, 1443, 1445, 1447, 1449, 1451, 1453, 1455, 1457, 1459, 1461, 1463, 1465, 1467, 1469, 1471, 1473, 1475, 1477, 1479, 1481, 1483, 1485, 1487, 1489, 1491, 1493, 1495, 1497, 1499, 1501, 1503, 1505, 1507, 1509, 1511, 1513, 1515, 1517, 1519, 1521, 1523, 1525, 1527, 1529, 1531, 1533, 1535, 1537, 1539, 1541, 1543, 1545, 1547, 1549, 1551, 1553, 1555, 1557, 1559, 1561, 1563, 1565, 1567, 1569, 1571, 1573, 1575, 1577, 1579, 1581, 1583, 1585, 1587, 1589, 1591, 1593, 1595, 1597, 1599, 1601, 1603, 1605, 1607, 1609, 1611, 1613, 1615, 1617, 1619, 1621, 1623, 1625, 1627, 1629, 1631, 1633, 1635, 1637, 1639, 1641, 1643, 1645, 1647, 1649, 1651, 1653, 1655, 1657, 1659, 1661, 1663, 1665, 1667, 1669, 1671, 1673, 1675, 1677, 1679, 1681, 1683, 1685, 1687, 1689, 1691, 1693, 1695, 1697, 1699, 1701, 1703, 1705, 1707, 1709, 1711, 1713, 1715, 1717, 1719, 1721, 1723, 1725, 1727, 1729, 1731, 1733, 1735, 1737, 1739, 1741, 1743, 1745, 1747, 1749, 1751, 1753, 1755, 1757, 1759, 1761, 1763, 1765, 1767, 1769, 1771, 1773, 1775, 1777, 1779, 1781, 1783, 1785, 1787, 1789, 1791, 1793, 1795, 1797, 1799, 1801, 1803, 1805, 1807, 1809, 1811, 1813, 1815, 1817, 1819, 1821, 1823, 1825, 1827, 1829, 1831, 1833, 1835, 1837, 1839, 1841, 1843, 1845, 1847, 1849, 1851, 1853, 1855, 1857, 1859, 1861, 1863, 1865, 1867, 1869, 1871, 1873, 1875, 1877, 1879, 1881, 1883, 1885, 1887, 1889, 1891, 1893, 1895, 1897, 1899, 1901, 1903, 1905, 1907, 1909, 1911, 1913, 1915, 1917, 1919, 1921, 1923, 1925, 1927, 1929, 1931, 1933, 1935, 1937, 1939, 1941, 1943, 1945, 1947, 1949, 1951, 1953, 1955, 1957, 1959, 1961, 1963, 1965, 1967, 1969, 1971, 1973, 1975, 1977, 1979, 1981, 1983, 1985, 1987, 1989, 1991, 1993, 1995, 1997, 1999, 2001, 2003, 2005, 2007, 2009, 2011, 2013, 2015, 2017, 2019, 2021, 2023, 2025, 2027, 2029, 2031, 2033, 2035, 2037, 2039, 2041, 2043, 2045, 2047, 2049, 2051, 2053, 2055, 2057, 2059, 2061, 2063, 2065, 2067, 2069, 2071, 2073, 2075, 2077, 2079, 2081, 2083, 2085, 2087, 2089, 2091, 2093, 2095, 2097, 2099, 2101, 2103, 2105, 2107, 2109, 2111, 2113, 2115, 2117, 2119, 2121, 2123, 2125, 2127, 2129, 2131, 2133, 2135, 2137, 2139, 2141, 2143, 2145, 2147, 2149, 2151, 2153, 2155, 2157, 2159, 2161, 2163, 2165, 2167, 2169, 2171, 2173, 2175, 2177, 2179, 2181, 2183, 2185, 2187, 2189, 2191, 2193, 2195, 2197, 2199, 2201, 2203, 2205, 2207, 2209, 2211, 2213, 2215, 2217, 2219, 2221, 2223, 2225, 2227, 2229, 2231, 2233, 2235, 2237, 2239, 2241, 2243, 2245, 2247, 2249, 2251, 2253, 2255, 2257, 2259, 2261, 2263, 2265, 2267, 2269, 2271, 2273, 2275, 2277, 2279, 2281, 2283, 2285, 2287, 2289, 2291, 2293, 2295, 2297, 2299, 2301, 2303, 2305, 2307, 2309, 2311, 2313, 2315, 2317, 2319, 2321, 2323, 2325, 2327, 2329, 2331, 2333, 2335, 2337, 2339, 2341, 2343, 2345, 2347, 2349, 2351, 2353, 2355, 2357, 2359, 2361, 2363, 2365, 2367, 2369, 2371, 2373, 2375, 2377, 2379, 2381, 2383, 2385, 2387, 2389, 2391, 2393, 2395, 2397, 2399, 2401, 2403, 2405, 2407, 2409, 2411, 2413, 2415, 2417, 2419, 2421, 2423, 2425, 2427, 2429, 2431, 2433, 2435, 2437, 2439, 2441, 2443, 2445, 2447, 2449, 2451, 2453, 2455, 2457, 2459, 2461, 2463, 2465, 2467, 2469, 2471, 2473, 2475, 2477, 2479, 2481, 2483, 2485, 2487, 2489, 2491, 2493, 2495, 2497, 2499, 2501, 2503, 2505, 2507, 2509, 2511, 2513, 2515, 2517, 2519, 2521, 2523, 2525, 2527, 2529, 2531, 2533, 2535, 2537, 2539, 2541, 2543, 2545, 2547, 2549, 2551, 2553, 2555, 2557, 2559, 2561, 2563, 2565, 2567, 2569, 2571, 2573, 2575, 2577, 2579, 2581, 2583, 2585, 2587, 2589, 2591, 2593, 2595, 2597, 2599, 2601, 2603, 2605, 2607, 2609, 2611, 2613, 2615, 2617, 2619, 2621, 2623, 2625, 2627, 2629, 2631, 2633, 2635, 2637, 2639, 2641, 2643, 2645, 2647, 2649, 2651, 2653, 2655, 2657, 2659, 2661, 2663, 2665, 2667, 2669, 2671, 2673, 2675, 2677, 2679, 2681, 2683, 2685, 2687, 2689, 2691, 2693, 2695, 2697, 2699, 2701, 2703, 2705, 2707, 2709, 2711, 2713, 2715, 2717, 2719, 2721, 2723, 2725, 2727, 2729, 2731, 2733, 2735, 2737, 2739, 2741, 2743, 2745, 2747, 2749, 2751, 2753, 2755, 2757, 2759, 2761, 2763, 2765, 2767, 2769, 2771, 2773, 2775, 2777, 2779, 2781, 2783, 2785, 2787, 2789, 2791, 2793, 2795, 2797, 2799, 2801, 2803, 2805, 2807, 2809, 2811, 2813, 2815, 2817, 2819, 2821, 2823, 2825, 2827, 2829, 2831, 2833, 2835, 2837, 2839, 2841, 2843, 2845, 2847, 2849, 2851, 2853, 2855, 2857, 2859, 2861, 2863, 2865, 2867, 2869, 2871, 2873, 2875, 2877, 2879, 2881, 2883, 2885, 2887, 2889, 2891, 2893, 2895, 2897, 2899, 2901, 2903, 2905, 2907, 2909, 2911, 2913, 2915, 2917, 2919, 2921, 2923, 2925, 2927, 2929, 2931, 2933, 2935, 2937, 2939, 2941, 2943, 2945, 2947, 2949, 2951, 2953, 2955, 2957, 2959, 2961, 2963, 2965, 2967, 2969, 2971, 2973, 2975, 2977, 2979, 2981, 2983, 2985, 2987, 2989, 2991, 2993, 2995, 2997, 2999, 3001, 3003, 3005, 3007, 3009, 3011, 3013, 3015, 3017, 3019, 3021, 3023, 3025, 3027, 3029, 3031, 3033, 3035, 3037, 3039, 3041, 3043, 3045, 3047, 3049, 3051, 3053, 3055, 3057, 3059, 3061, 3063, 3065, 3067, 3069, 3071, 3073, 3075, 3077, 3079, 3081, 3083, 3085, 3087, 3089, 3091, 3093, 3095, 3097, 3099, 3101, 3103, 3105, 3107, 3109, 3111, 3113, 3115, 3117, 3119, 3121, 3123, 3125, 3127, 3129, 3131, 3133, 3135, 3137, 3139, 3141, 3143, 3145, 3147, 3149, 3151, 3153, 3155, 3157, 3159, 3161, 3163, 3165, 3167, 3169, 3171, 3173, 3175, 3177, 3179, 3181, 3183, 3185, 3187, 3189, 3191, 3193, 3195, 3197, 3199, 3201, 3203, 3205, 3207, 3209, 3211, 3213, 3215"

            ss=ss.replaceAll(" ", '')


            ss.split(",").each {id ->








                MultipartEntity reqEntity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);

                reqEntity.addPart("xml",
                        new StringBody("""<baseContent>
          <UUID>a9c2f862-f0bc-46ca-ac15-8ac7515365de</UUID>
          <ownerBundle id="${id}"/>
          <description>ECHOOcontent beschrijving1</description>
          <title>content titel1</title>
          <url>ECHO  www.content-url1.nl</url>
    </baseContent>"""));

                FileBody bin = new FileBody(new File(testService.getAbsolutePath('data', "bla.jpg")));
                reqEntity.addPart("file", bin);



                httppost.setEntity(reqEntity);

                2.times {i ->
                    //System.out.println("executing request " + httppost.getRequestLine());
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity resEntity = response.getEntity();
                    //System.out.println("DONEEE ${i}");


                    if (resEntity != null)
                    {
                        String page = EntityUtils.toString(resEntity);

                        System.out.println("PAGE :" + page);
                        return page
                    }

                }

                /*httppost.addHeader("Content-type", "multipart/mixed")*/
            }


        }



    def upload =
        {
            System.out.println('Start upload REST ');

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost('http://localhost:8080/catchplus/api/baseContent');

            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);

            reqEntity.addPart("xml",
                    new StringBody("""<baseContent>
          <UUID>a9c2f862-f0bc-46ca-ac15-8ac7515365de</UUID>
          <ownerBundle id="860632"/>
          <description>ECHOOcontent beschrijving1</description>
          <title>content titel1</title>
          <url>ECHO  www.content-url1.nl</url>
    </baseContent>"""));

            FileBody bin = new FileBody(new File(testService.getAbsolutePath('data', "bla.jpg")));
            //reqEntity.addPart("image", bin);
            reqEntity.addPart("file", bin);



            httppost.setEntity(reqEntity);

            /*httppost.addHeader("Content-type", "multipart/mixed")*/

            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            System.out.println("DONEEE");


            if (resEntity != null)
            {
                String page = EntityUtils.toString(resEntity);

                System.out.println("PAGE :" + page);
                return page
            }
        }

    def test2 = {
        Collection c = Collection.get(35)
             //list.split(";").each
             List b = c.bundle*.id
             b.toListString()
             println b.toListString()




    }

    def login = {
        return [username: params.username, rememberMe: (params.rememberMe != null), targetUri: params.targetUri]
    }

    def app = {

        if (!SecurityUtils.subject.authenticated)
        {
            redirect(controller: 'auth', action: 'login')
        }


        if (shiroService.account(true))
            [locale: new Locale(shiroService.account(true).language)]

        /*def workSpaceList = WorkSpace.list()
     [workSpaceList: workSpaceList, renderWorkSpace: true]*/
    }

    def mirror = {
        println 'mirror off'
        searchableService.stopMirroring()
    }

    def mirror2 = {
        println 'mirror on'
        searchableService.startMirroring()
    }


    def home = {
        //println 'app2'
        /*if (!SecurityUtils.subject.authenticated)
        {
            redirect(controller: 'auth', action: 'login')
        }*/

        /*def workSpaceList = WorkSpace.list()
     [workSpaceList: workSpaceList, renderWorkSpace: true]*/
    }

/*
    def help =
    {
        redirect(url: 'http://ww.nu.nl')

    }
*/
    def sall =
        {

            //Collection collection = Collection.list()[1]

            //  def user = ShiroUser.get(119)
            def user = shiroService.getActiveShiroUser(true)// ShiroUser.get(106)

            //session.activeShiroUserId2=106

            def institution2 = user.institution
            //Bundle bundle = Bundle.get(138)


            ArrayList list = new ArrayList()



            println 'sall1abcefgh'
            BaseDomain.bootstrap = true
            //  searchableService.stopMirroring()

            def time = System.currentTimeMillis()


            int j = 0
            //while (true)
            /*while (true)
            {*/
                Collection.list().each {collection ->


                    1.times
                            {

                                Bundle bundle

                                1000.times {
                                    bundle = new Bundle()
                                    bundle.title = 'AAP'
                                    bundle.createdBy = user
                                    bundle.description = 'AAP2'
                                    bundle.owner = institution2
                                    bundle.ownerCollection = collection

                                    bundle.store()
                                    //  collection.addToBundle(bundle)

                                    //bundle.store()
                                    // println collection.bundle
                                    //collection.addBundle(bundle)

                                    //println collection.bundle

                                    //   collection.validate()
                                    //println collection.errors

                                    //collection.save(flush:true)
                                    //    collection.refresh()
                                    //println collection.bundle

                                    BaseContent content

                                    1.times {i ->
                                        content = new BaseContent()
                                        content.title = 'AAP'
                                        content.createdBy = user
                                        content.description = 'AAP2'
                                        content.url = 'AAP3'
                                        content.owner = institution2
                                        content.ownerBundle = bundle
                                        /*if (i == 999)
                                        {
                                            println 'flushing'
                                            content.save(flush: true, validate: false)
                                            println 'done flushing'

                                        }
                                        else*/
                                        //   content.save()

                                        //list.add(content)
                                        // bundle.addToContent(content)
                                        content.store()


                                    }
                                    println 'sqling'
                                    /*list.each { baseContent ->
                                        bundle.addContent(baseContent)

                                    }*/
                                    println 'done sqling'
                                    list.clear()
                                    bundle = null
                                    content = null
                                    println 'bundle ' + j + " : " + (System.currentTimeMillis() - time)
                                    log.info 'bundle ' + j + " : " + (System.currentTimeMillis() - time)
                                    time = System.currentTimeMillis()
                                    org.hibernate.classic.Session sessionn = sessionFactory.getCurrentSession()
                                    sessionn.flush()
                                    sessionn.clear()
                                    def ra = RequestContextHolder.currentRequestAttributes()
                                    RequestContextHolder.resetRequestAttributes()//httpSessionService.test()
                                    RequestContextHolder.setRequestAttributes(ra)
                                    j++


                                }

                            }

                }
            //}
        }



    def sall2 =
        {

            //Collection collection = Collection.list()[1]

            //  def user = ShiroUser.get(119)
            def user = shiroService.getActiveShiroUser(true)
            def institution2 = user.institution
            //Bundle bundle = Bundle.get(138)


            ArrayList list = new ArrayList()



            println 'sall1'
            BaseDomain.bootstrap = true
            searchableService.stopMirroring()

            def time = System.currentTimeMillis()


            int j = 0
            //while (true)

            Collection.list().each {collection ->

                while (true)
                //1.times
                {

                    Bundle bundle

                    1000.times {

                        bundle = new Bundle()
                        bundle.title = 'AAP'
                        bundle.createdBy = user
                        bundle.description = 'AAP2'
                        bundle.owner = institution2
                        bundle.ownerCollection = collection
                        bundle.save(flush: true, validate: false)

                        collection.addBundle(bundle)

                        BaseContent content

                        1000.times {i ->
                            //println i
                            content = new BaseContent()
                            content.title = 'AAP'
                            content.createdBy = user
                            content.description = 'AAP2'
                            content.url = 'AAP3'
                            content.owner = institution2
                            content.ownerBundle = bundle

                            if (i == 999)
                            {
                                println 'flushing'
                                content.save(flush: true, validate: false)

                            }
                            else
                                content.save(validate: false)

                            list.add(content)


                        }
                        println 'sqling'
                        list.each { baseContent ->
                            //println 'baseContent'
                            bundle.addContent(baseContent)

                        }
                        println 'clear'
                        list.clear()
                        bundle = null
                        content = null
                        println 'bundle ' + j + " : " + (System.currentTimeMillis() - time)
                        time = System.currentTimeMillis()
                        org.hibernate.classic.Session sessionn = sessionFactory.getCurrentSession()
                        sessionn.flush()
                        sessionn.clear()
                        /*def ra = RequestContextHolder.currentRequestAttributes()
                        RequestContextHolder.resetRequestAttributes()//httpSessionService.test()
                        RequestContextHolder.setRequestAttributes(ra)*/
                        j++


                    }

                }

            }
        }


    def signIn = {

        if (false/*curityUtils.subject.isAuthenticated()*/)
            redirect(action: 'app')
        else
        {
            println params
            def authToken = new UsernamePasswordToken(params.username?.trim(), params.password?.trim() as String)

            // Support for "remember me"
            if (params.rememberMe)
            {
                authToken.rememberMe = true
            }

            // If a controller redirected to this page, redirect back
            // to it. Otherwise redirect to the root URI.
            def targetUri = params.targetUri ?: "/application"

            // Handle requests saved by Shiro filters.
            def savedRequest = WebUtils.getSavedRequest(request)
            if (savedRequest)
            {
                targetUri = savedRequest.requestURI - request.contextPath
                if (savedRequest.queryString) targetUri = targetUri + '?' + savedRequest.queryString
            }

            try
            {
                // Perform the actual login. An AuthenticationException
                // will be thrown if the username is unrecognised or the
                // password is incorrect.

                session.activeShiroUser = null


                SecurityUtils.subject.login(authToken)

                ShiroUser.withTransaction {
                    if (session.activeShiroUser)
                    {
                        shiroService.getActiveShiroUser(false).lastLogOn = new Date()
                    }
                }

                Account.withTransaction {
                    shiroService.account(false).lastLogOn = new Date()
                    /*user.lastLogOn = new Date()
                   user.save(flush: true)*/
                }

                if (params.activeShiroUser)
                {
                    long userId = Long.parseLong(params.activeShiroUser)

                    if (ShiroUser.countByAccountAndId(shiroService.account(true), userId) > 0)
                    {
                        session.activeShiroUser = userId
                    }

                }
                log.info "Redirecting to '${targetUri}'."
                redirect(uri: targetUri)
            }



            catch (AuthenticationException ex)
            {
                if(ex instanceof NotActivatedAccountException )
                    emailService.sendRequestNewPassword(Account.findByUsername(params.username))

                //System.out.println(ex.message)
                // Authentication failed, so display the appropriate message
                // on the login page.
                log.info "Authentication failure for user '${params.username}'."
                flash.message = ex.message// message(code: "login.failed")

                // Keep the username and "remember me" setting so that the
                // user doesn't have to enter them again.
                def m = [username: params.username]
                if (params.rememberMe)
                {
                    m["rememberMe"] = true
                }

                // Remember the target URI too.
                if (params.targetUri)
                {
                    m["targetUri"] = params.targetUri
                }

                // Now redirect back to the login page.
                redirect(action: "login", params: m)
            }
        }
    }

    def signOut = {
        // Log the user out of the application.
        SecurityUtils.subject?.logout()

        // For now, redirect back to the home page.
        redirect(uri: "/")
    }

    def unauthorized = {
        render "You do not have permission to access this page."
    }

    def choose = {

        println 'test'
        def ra = RequestContextHolder.currentRequestAttributes()
        RequestContextHolder.resetRequestAttributes()//httpSessionService.test()
        RequestContextHolder.setRequestAttributes(ra)

    }

}
