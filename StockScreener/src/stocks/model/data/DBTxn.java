package stocks.model.data;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;

import javax.sql.DataSource;
public class DBTxn {
	public static DBTxn INSTANCE= new DBTxn();
	public DataSource DS = null;
	private DBTxn () {
			try {
				Class.forName( "oracle.jdbc.driver.OracleDriver" );
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ConnectionFactory connectionFactory =new DriverManagerConnectionFactory("jdbc:oracle:thin:stocks/linuxing@172.0.0.1:1521:XE",null);
			PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
			ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
			poolableConnectionFactory.setPool(connectionPool);
			DS = new PoolingDataSource<>(connectionPool);
	}
}
