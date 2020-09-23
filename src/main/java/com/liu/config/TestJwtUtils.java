package com.liu.config;

import com.liu.entity.CheckUserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestJwtUtils {

    //token请求头
    public static final String TOKEN_HEADEA = "Authorization";

    //令牌前缀
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String SUBJECT = "liu";

    //失效时间
    public static final long EXPIRITION = 1000 * 24 * 60 * 60 * 7;

    //密码岩  可以用方法进行生成
    public static final String APPSECRET_KEY = "liu_secret";

    //角色
    private static final String ROLE_CLAIMS = "rol";


    public static String generateJsonWebToken(CheckUserEntity checkUserEntity) {
        if (checkUserEntity.getUserId() == null || checkUserEntity.getUserName() == null /*|| dateUserEntity.getFaceImage() == null*/) {
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, "rol");

        String token = Jwts.builder() // 创建 JWT 对象
                .setSubject(SUBJECT)  // 设置主题（声明信息,作者）
                .setClaims(map)
                .claim("userId", checkUserEntity.getUserId())
                .claim("userName", checkUserEntity.getUserName())
                .claim("password", checkUserEntity.getPassword())
                //创建时间
                .setIssuedAt(new Date())
                //签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRITION))
                //签名方法 两个参数分别是签名算法和自定义的签名Key（盐）。
                // 签名key可以byte[] 、String及Key的形式传入。
                // 前两种形式均存入builder的keyBytes属性，
                // 后一种形式存入builder的key属性。
                // 如果是第二种（及String类型）的key，则将其进行base64解码获得byte[]
                .signWith(SignatureAlgorithm.HS256, APPSECRET_KEY).compact();
        return token;
    }
    /**
     * 生成token
     * @param username
     * @param role
     * @return
     */
    public static String createToken(String username,String role) {
        Map<String,Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        String token = Jwts
                .builder()
                .setSubject(username)
                .setClaims(map)
                .claim("username",username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRITION))
                .signWith(SignatureAlgorithm.HS256, APPSECRET_KEY).compact();
        return token;
    }

    //检查token 生成签名
    public static Claims checkJWT(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    // TODO: 2020/5/26  这个代码 通过拦截器进行拦截获得   看看公司代码是怎么写的
    public static String getUsername(String token){
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get("username").toString();
    }

    /**
     * 获取用户角色
     * @param token
     * @return
     */
    public static String getUserRole(String token){
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get("rol").toString();
    }

    /**
     * 是否过期
     * @param token
     * @return
     */
    public static boolean isExpiration(String token){
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }

    /*public static void main(String[] args) {
        String name = "acong";
        String role = "rol";
        String token = createToken(name,role);
        System.out.println(token);

        Claims claims = checkJWT(token);
        System.out.println(claims.get("username"));

        System.out.println(getUsername(token));
        System.out.println(getUserRole(token));
        System.out.println(isExpiration(token));
    }*/

    /*public static void main(String[] args) {

		DateUserEntity user = new DateUserEntity();
		user.setUserId("11011");
		user.setUserName("nuoweisiki");
		//user.setFaceImage("www.uoko.com/1.png");
		String token = generateJsonWebToken(user);

		System.out.println(token);

		Claims claims = checkJWT(token);
		if (claims != null) {
			String id = claims.get("userId").toString();
			String name = claims.get("userName").toString();
			//String img = claims.get("img").toString();

			String rol = claims.get("rol").toString();

			System.out.println("id:" + id);
			System.out.println("name:" + name);
			//System.out.println("img:" + img);

			System.out.println("rol:" + rol);

		}

	}*/
}
