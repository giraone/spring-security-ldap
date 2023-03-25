package com.giraone.webmvc.security.config;

public class LdapProperties {

    private final String ldapUrl = "ldap://localhost:1389";
    private final String basePath = "dc=example,dc=org";
    // private final String bindUser = "cn=manager,dc=example,dc=org"; // DN
    private final String bindUser = "cn=manager"; // CN with appended basepath
    private final String bindPassword = "secret";
    private final UserSearch userSearch = new UserSearch();
    private final GroupSearch groupSearch = new GroupSearch();

    public LdapProperties() {
    }

    public String getLdapUrl() {
        return ldapUrl;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getBindUser() {
        return bindUser;
    }

    public String getBindPassword() {
        return bindPassword;
    }

    public UserSearch getUserSearch() {
        return userSearch;
    }

    public GroupSearch getGroupSearch() {
        return groupSearch;
    }

    @Override
    public String toString() {
        return "LdapProperties{" +
            "ldapUrl='" + ldapUrl + '\'' +
            ", basePath='" + basePath + '\'' +
            ", bindUser='" + bindUser + '\'' +
            ", bindPassword='" + bindPassword + '\'' +
            ", userSearch=" + userSearch +
            '}';
    }

    //------------------------------------------------------------------------------------------------------------------

    public static class UserSearch {
        private final String userDnPatterns = "cn={0},ou=users";
        private final String userSearchBase = "ou=users";
        private final String userSearchFilter = "(uid={0})";
        private final String passwordAttribute = "userPassword";

        public UserSearch() {
        }

        public String getUserDnPatterns() {
            return userDnPatterns;
        }

        public String getUserSearchBase() {
            return userSearchBase;
        }

        public String getUserSearchFilter() {
            return userSearchFilter;
        }

        public String getPasswordAttribute() {
            return passwordAttribute;
        }

        @Override
        public String toString() {
            return "UserSearch{" +
                "userDnPatterns='" + userDnPatterns + '\'' +
                ", userSearchBase='" + userSearchBase + '\'' +
                ", userSearchFilter='" + userSearchFilter + '\'' +
                ", passwordAttribute='" + passwordAttribute + '\'' +
                '}';
        }
    }

    public static class GroupSearch {
        private final String groupSearchBase = "ou=groups";
        private final String groupSearchFilter = "member={0}";

        public GroupSearch() {
        }

        public String getGroupSearchBase() {
            return groupSearchBase;
        }

        public String getGroupSearchFilter() {
            return groupSearchFilter;
        }

        @Override
        public String toString() {
            return "GroupSearch{" +
                "groupSearchBase='" + groupSearchBase + '\'' +
                ", groupSearchFilter='" + groupSearchFilter + '\'' +
                '}';
        }
    }
}
