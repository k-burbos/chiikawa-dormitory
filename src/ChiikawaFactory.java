public class ChiikawaFactory {
    public Tenant createChiikawa(String name) {
        switch(name) {
            case "Chiikawa":
                return new Tenant.Chiikawa();
            case "Furuhonya":
                return new Tenant.Furuhonya();
            case "Hachiware":
                return new Tenant.Hachiware();
            case "Momonga":
                return new Tenant.Momonga();
            case "Rakko":
                return new Tenant.Rakko();
            case "Shisa":
                return new Tenant.Shisa();
            case "Usagi":
                return new Tenant.Usagi();
            case "Kurimanju":
                return new Tenant.Kurimanju();
        }
        return null;
    }
}
