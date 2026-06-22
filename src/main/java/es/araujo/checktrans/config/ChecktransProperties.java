package es.araujo.checktrans.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "checktrans")
public class ChecktransProperties {

    private App app = new App();
    private Pagination pagination = new Pagination();
    private Audit audit = new Audit();
    private Checklist checklist = new Checklist();

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public static class App {
        private String name = "CHECKTRANS";
        private String version = "1.0.0";
        private String description = "Sistema de Gestion de Listas de Chequeo de Transportes";
        private String locale = "es_ES";
        private String timezone = "Europe/Madrid";

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getLocale() { return locale; }
        public void setLocale(String locale) { this.locale = locale; }
        public String getTimezone() { return timezone; }
        public void setTimezone(String timezone) { this.timezone = timezone; }
    }

    public static class Pagination {
        private int defaultPageSize = 10;
        private int maxPageSize = 100;

        public int getDefaultPageSize() { return defaultPageSize; }
        public void setDefaultPageSize(int defaultPageSize) { this.defaultPageSize = defaultPageSize; }
        public int getMaxPageSize() { return maxPageSize; }
        public void setMaxPageSize(int maxPageSize) { this.maxPageSize = maxPageSize; }
    }

    public static class Audit {
        private boolean enabled = true;
        private boolean includeCreatedAt = true;
        private boolean includeUpdatedAt = true;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public boolean isIncludeCreatedAt() { return includeCreatedAt; }
        public void setIncludeCreatedAt(boolean includeCreatedAt) { this.includeCreatedAt = includeCreatedAt; }
        public boolean isIncludeUpdatedAt() { return includeUpdatedAt; }
        public void setIncludeUpdatedAt(boolean includeUpdatedAt) { this.includeUpdatedAt = includeUpdatedAt; }
    }

    public static class Checklist {
        private String codePrefix = "CT-";
        private int maxItems = 50;
        private int maxObservationsLength = 2000;
        private String allowedStatus = "DRAFT, COMPLETED, APPROVED, REJECTED";

        public String getCodePrefix() { return codePrefix; }
        public void setCodePrefix(String codePrefix) { this.codePrefix = codePrefix; }
        public int getMaxItems() { return maxItems; }
        public void setMaxItems(int maxItems) { this.maxItems = maxItems; }
        public int getMaxObservationsLength() { return maxObservationsLength; }
        public void setMaxObservationsLength(int maxObservationsLength) { this.maxObservationsLength = maxObservationsLength; }
        public String getAllowedStatus() { return allowedStatus; }
        public void setAllowedStatus(String allowedStatus) { this.allowedStatus = allowedStatus; }
    }
}
