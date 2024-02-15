package pdf.utils;

public interface PropertiesConverter {

    public final String JWT_RANDOM_KEY = "#{new pdf.utils.KeyGenerator().jwtRandomKey(256,'${jwt.secret}')}";
    public final String SECRET_KEY = "#{new pdf.utils.KeyGenerator().key(256,'${key.password}')}";

}