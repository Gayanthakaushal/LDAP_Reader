package org.wso2;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * LDAP Test
 */
public class LDAPTest {

    public static void main(String[] args){

        Scanner sc=new Scanner(System.in);

        System.out.println("Enter LDAP URL");
        String LDAP_URL = sc.next();

        System.out.println("Enter LDAP USER");
        String LDAP_USER = sc.next();

        System.out.println("Enter LDAP Password");
        String LDAP_PASSWORD = sc.next();

        System.out.println("Enter LDAP Search Base");
        String LDAP_SEARCH_BASE = sc.next();

        System.out.println("Enter Keystore path");
        String KEYSTORE = sc.next();

        System.out.println("Enter Tenant User which you can successfully login");
        String SUCCESSFUL_USER = sc.next();

        System.out.println("Enter Tenant User which you get READ Timeout");
        String READ_TIMEOUT_USER = sc.next();

        if(args != null  && args.length == 2) {
            LDAP_PASSWORD = args[0];
            KEYSTORE = args[1];
        }

        System.setProperty("javax.net.ssl.trustStore", KEYSTORE);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

        Hashtable<String, String > environment = new Hashtable<String, String >();

        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.REFERRAL, "follow");
        environment.put(Context.PROVIDER_URL, LDAP_URL);
        environment.put(Context.SECURITY_PRINCIPAL, LDAP_USER);
        environment.put(Context.SECURITY_CREDENTIALS, LDAP_PASSWORD);

        DirContext ctx = null;
        NamingEnumeration<SearchResult> results = null;
        SearchControls searchControls = new SearchControls();
        SearchResult searchResult;

        System.out.println("============  Test is started  -  Successful User  ================");
        System.out.println("== TRUST STORE == : "  + KEYSTORE);

        for(int i = 0; i < 100; i++ ) {
            long t1 = System.currentTimeMillis();
            try {
                ctx = new InitialDirContext(environment);
                String searchFilter = "(&(objectClass="+SUCCESSFUL_USER+")(cn=ennesimoPT00000006.sercommGW))";
                //String searchFilter = "(&(objectClass=person)(cn=admin))";
                searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                results = ctx.search(LDAP_SEARCH_BASE, searchFilter, searchControls);

                while (results.hasMore()) {
                    try {
                        searchResult = results.next();
                        System.out.println("== Name == : " + searchResult.getNameInNamespace());
                    } catch (Exception e) {
                        System.out.println("ERROR is occurred with Next");
                        e.printStackTrace();
                    }
                }
            } catch (NamingException e) {
                System.out.println("ERROR is occurred while searching!");
                e.printStackTrace();
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (NamingException e) {
                        e.printStackTrace();
                    }
                }
                if (ctx != null) {
                    try {
                        ctx.close();
                    } catch (NamingException e) {
                        e.printStackTrace();
                    }
                }
            }

            long t2 = System.currentTimeMillis();

            System.out.println("============  Time for searching a LDAP Query :  " +  (t2-t1)  + " ms ================");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("=====  Test is finished  -  Successful User  =====");
        System.out.println("============  Test is started  -  READ Timeout User  ================");
        System.out.println("== TRUST STORE == : "  + KEYSTORE);

        for(int i = 0; i < 100; i++ ) {
            long t1 = System.currentTimeMillis();

            try {
                ctx = new InitialDirContext(environment);
                String searchFilter = "(&(objectClass="+READ_TIMEOUT_USER+")(cn=ennesimoPT00000006.sercommGW))";
                searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                results = ctx.search(LDAP_SEARCH_BASE, searchFilter, searchControls);

                while (results.hasMore()) {
                    try {
                        searchResult = results.next();
                        System.out.println("== Name == : " + searchResult.getNameInNamespace());
                    } catch (Exception e) {
                        System.out.println("ERROR is occurred with Next");
                        e.printStackTrace();
                    }
                }
            } catch (NamingException e) {
                System.out.println("ERROR is occurred while searching!");
                e.printStackTrace();
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (NamingException e) {
                        e.printStackTrace();
                    }
                }
                if (ctx != null) {
                    try {
                        ctx.close();
                    } catch (NamingException e) {
                        e.printStackTrace();
                    }
                }
            }

            long t2 = System.currentTimeMillis();

            System.out.println("============  Time for searching a LDAP Query :  " +  (t2-t1)  + " ms ================");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("=====  Test is finished  -  READ Timeout User  =====");

        System.exit(0);
    }
}
