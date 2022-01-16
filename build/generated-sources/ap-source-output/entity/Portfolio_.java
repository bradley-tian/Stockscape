package entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-03-21T01:36:18")
@StaticMetamodel(Portfolio.class)
public class Portfolio_ { 

    public static volatile SingularAttribute<Portfolio, String> stockname;
    public static volatile SingularAttribute<Portfolio, String> stockexchange;
    public static volatile SingularAttribute<Portfolio, Double> shareprice;
    public static volatile SingularAttribute<Portfolio, String> stockticker;
    public static volatile SingularAttribute<Portfolio, Double> equitycap;
    public static volatile SingularAttribute<Portfolio, Integer> id;
    public static volatile SingularAttribute<Portfolio, Integer> userId;
    public static volatile SingularAttribute<Portfolio, Double> sharesowned;

}