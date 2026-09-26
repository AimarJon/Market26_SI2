package dataAccess;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import businessLogic.BLFacadeImplementation;
import configuration.ConfigXML;
import configuration.UtilDate;
import enums.MovementType;
import enums.OfferStatusType;
import enums.ReportReason;
import enums.RequestStatusType;
import enums.SaleStatusType;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.NotEnoughMoneyException;
import exceptions.SaleAlreadyExistException;

import domain.*;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;
	private static final int baseSize = 160;

	private static final String basePath="src/main/resources/images/";

    private static final Logger logger = LoggerFactory.getLogger(DataAccess.class);
    
    private static final String RESOURCE_BUNDLE_NAME = "Etiquetas";


	ConfigXML c=ConfigXML.getInstance();

	public DataAccess()  {
		if (c.isDatabaseInitialized()) {
			String fileName=c.getDbFilename();

			File fileToDelete= new File(fileName);
			if(fileToDelete.delete()){
				File fileToDeleteTemp= new File(fileName+"$");
				fileToDeleteTemp.delete();
				System.out.println("File deleted");
			} else {
				System.out.println("Operation failed");
			}
		}
		open();
		if  (c.isDatabaseInitialized()) 
			initializeDB();
		System.out.println("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());

		close();

	}

	public DataAccess(EntityManager db) {
		this.db=db;
	}



	/**
	 * This method  initializes the database with some products and sellers.
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	public void initializeDB(){

		db.getTransaction().begin();

		try { 

			//Create sellers 
			Registered seller1=new Registered("seller1@gmail.com","123","123");
			Registered seller2=new Registered("seller22@gmail.com","Ane Gaztañaga","123");
			Registered seller3=new Registered("seller3@gmail.com","Test Seller","123");


			//Create products
			Date today = UtilDate.trim(new Date());

			//Create admin
			Admin admin1=new Admin("admin1@admin.com", "123");




			seller1.addSale("futbol baloia", "oso polita, gutxi erabilita", 2, 10,  today, null);
			seller1.addSale("salomon mendiko botak", "44 zenbakia, 3 ateraldi",2,  20,  today, null);
			seller1.addSale("samsung 42\" telebista", "berria, erabili gabe", 1, 175,  today, null);


			seller2.addSale("imac 27", "7 urte, dena ondo dabil", 1, 200,today, null);
			seller2.addSale("iphone 17", "oso gutxi erabilita", 2, 400, today, null);
			seller2.addSale("orbea mendiko bizikleta", "29\" 10 urte, mantenua behar du", 3,225, today, null);
			seller2.addSale("polar kilor erlojua", "Vantage M, ondo dago", 3, 30, today, null);

			seller3.addSale("sukaldeko mahaia", "1.8*0.8, 4 aulkiekin. Prezio finkoa", 3,45, today, null);


			db.persist(seller1);
			db.persist(seller2);
			db.persist(seller3);

			db.persist(admin1);


			db.getTransaction().commit();
			System.out.println("Db initialized");
		}
		catch (Exception e){
			logger.error("context: ", e);
		}
	}


	/**
	 * This method creates/adds a product to a seller
	 * 
	 * @param title of the product
	 * @param description of the product
	 * @param status 
	 * @param selling price
	 * @param category of a product
	 * @param publicationDate
	 * @return Product
	 * @throws SaleAlreadyExistException if the same product already exists for the seller
	 */
	public Sale createSale(String title, String description, int status, float price,  Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {


		System.out.println(">> DataAccess: createProduct=> title= "+title+" seller="+sellerEmail);
		try {


			if(pubDate.before(UtilDate.trim(new Date()))) {
				throw new MustBeLaterThanTodayException(ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("DataAccess.ErrorSaleMustBeLaterThanToday"));
			}
			if (file==null)
				throw new FileNotUploadedException(ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("DataAccess.ErrorFileNotUploadedException"));

			db.getTransaction().begin();

			Registered seller = db.find(Registered.class, sellerEmail);
			if (seller.doesSaleExist(title)) {
				db.getTransaction().commit();
				throw new SaleAlreadyExistException(ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("DataAccess.SaleAlreadyExist"));
			}

			Sale sale = seller.addSale(title, description, status, price, pubDate, file);
			//next instruction can be obviated

			db.persist(seller); 
			db.getTransaction().commit();
			System.out.println("sale stored "+sale+ " "+seller);



			System.out.println("hasta aqui");

			return sale;
		} catch (NullPointerException e) {
			e.printStackTrace();
			db.getTransaction().commit();
			return null;
		}


	}

	/**
	 * This method retrieves all the products that contain a desc text in a title
	 * 
	 * @param desc the text to search
	 * @return collection of products that contain desc in a title
	 */
	/*public List<Sale> getSales(String desc) {
		System.out.println(">> DataAccess: getProducts=> from= "+desc);

		List<Sale> res = new ArrayList<Sale>();	
		TypedQuery<Sale> query = db.createQuery("SELECT s FROM Sale s WHERE s.title LIKE ?1",Sale.class);   
		query.setParameter(1, "%"+desc+"%");

		List<Sale> sales = query.getResultList();
		for (Sale sale:sales){
			res.add(sale);
		}
		return res;
	} */

	/**
	 * This method retrieves the products that contain a desc text in a title and the publicationDate today or before
	 * 
	 * @param desc the text to search
	 * @return collection of products that contain desc in a title
	 */
	public List<Sale> getPublishedSales(String desc, Date pubDate, String email) {
		System.out.println(">> DataAccess: getProducts=> from= " + desc);

		TypedQuery<Sale> query = db.createQuery(
				"SELECT s FROM Sale s WHERE s.title LIKE ?1 AND s.pubDate <= ?2 AND (s.saleStatus = ?3 OR s.saleStatus = ?4)",
				Sale.class);
		query.setParameter(1, "%" + desc + "%");
		query.setParameter(2, pubDate);
		query.setParameter(3, SaleStatusType.ON_SALE);
		query.setParameter(4, SaleStatusType.USER_REPORTED);

		List<Sale> sales = query.getResultList();
		ArrayList<Sale> ema = new ArrayList<Sale>();
		for (Sale s : sales) {
			if (!s.getSeller().getEmail().equals(email)) {
				ema.add(s);
			}
		}

		return ema;
	}

	public List<Sale> getPublishedSales(String desc, Date pubDate, String buyerEmail, String sellerMail, ArrayList<Sale> basket) {
		System.out.println(">> DataAccess: getProducts=> from= " + desc);

		TypedQuery<Sale> query = db.createQuery(
				"SELECT s FROM Sale s WHERE s.title LIKE ?1 AND s.pubDate <= ?2 AND (s.saleStatus = ?3 OR s.saleStatus = ?4)",
				Sale.class);
		query.setParameter(1, "%" + desc + "%");
		query.setParameter(2, pubDate);
		query.setParameter(3, SaleStatusType.ON_SALE);
		query.setParameter(4, SaleStatusType.USER_REPORTED);


		List<Sale> sales = query.getResultList();
		ArrayList<Sale> ema = new ArrayList<Sale>();
		System.out.println("Data acces basket: " + basket);
		for (Sale s : sales) {

			if (!s.getSeller().getEmail().equals(buyerEmail) && s.getSeller().getEmail().equals(sellerMail)) {
				System.out.println("s: " + s);
				if(!basket.contains(s))
					ema.add(s);
			}
		}
		return ema;
	}

	public List<Sale> getOnSales(String email, String filter) {
		System.out.println(">> DataAccess: getOnSales=> from= " + email);
		List<Sale> res = db.find(Registered.class, email).getSales();
		return getFiltered(res, filter);
	}
	public List<Sale> getWhisList(String email, String filter) {
		System.out.println(">> DataAccess: getWhisList=> from= "+email);
		List<Sale> res = db.find(Registered.class, email).getWishList();
		return getFiltered(res, filter);
	}
	public List<Sale> getPurchased(String email, String filter) {
		System.out.println(">> DataAccess: getBought => from= "+email);
		List<Sale> res = db.find(Registered.class, email).getBought();
		return getFiltered(res, filter);
	}

	private ArrayList<Sale> getFiltered(List<Sale> list, String filter){
		ArrayList<Sale> res = new ArrayList<Sale>();
		for (Sale s : list) {
			System.out.println("Sale filter: " + filter);
			if(s.getTitle().contains(filter)) res.add(s);
		}
		return res;
	}

	public List<Movement> getMovements(String email, MovementType type){
		TypedQuery<Movement> query = null;
		if(!type.equals(MovementType.ALL)) {
			query = db.createQuery("SELECT m FROM Movement m WHERE m.user.email = ?1 AND m.type = ?2",Movement.class);   
			query.setParameter(1, email);
			query.setParameter(2,type);	
		}else {
			query = db.createQuery("SELECT m FROM Movement m WHERE m.user.email = ?1",Movement.class);   
			query.setParameter(1, email);
		}
		List<Movement> movs = query.getResultList();
		return new ArrayList<Movement>(movs);

	}

	public List<Complaint> getComplaints(){
		TypedQuery<Complaint> query = db.createQuery("SELECT c FROM Complaint c WHERE c.treated = false",Complaint.class);   
		List<Complaint> list = query.getResultList();
		return new ArrayList<Complaint>(list);
	}

	public List<Report> getReports(){
		TypedQuery<Report> query = db.createQuery("SELECT r FROM Report r WHERE r.treated = false", Report.class);   
		return new ArrayList<Report>(query.getResultList());
	}


	public List<Request> getRequests(String email) {
		TypedQuery<Request> query = db.createQuery("SELECT r FROM Request r WHERE r.requestStatus =  ?1", Request.class);
		query.setParameter(1, RequestStatusType.AVAILABLE);

		ArrayList<Request> requests = new ArrayList<Request>();
		for(Request r:query.getResultList()) {
			if(!r.getRequester().getEmail().equals(email)) {
				requests.add(r);
			}
		}

		return requests;

	}



	public List<Offer> getOffers(String currentMail, String filter) {
		TypedQuery<Offer> query = db.createQuery("SELECT o FROM Offer o WHERE o.offerStatus = ?1", Offer.class);
		query.setParameter(1, OfferStatusType.WAITING);

		ArrayList<Offer> offers = new ArrayList<Offer>();
		for(Offer o:query.getResultList()) {
			Request r = o.getRequest();
			if(	r.getRequester().getEmail().equals(currentMail) && 
					r.getRequestStatus().equals(RequestStatusType.AVAILABLE) &&
					r.getTitle().contains(filter)) {
				System.out.println("getOffers, currentmail: "+currentMail);
				offers.add(o);
			}
		}

		return offers;

	}



	public boolean hasOffer(Request request, String currentUserMail) {
		TypedQuery<Offer> query = db.createQuery("SELECT o FROM Offer o", Offer.class);
		for(Offer o : query.getResultList()) {
			if(o.getRegistered().getEmail().equals(currentUserMail) && o.getRequest().equals(request))
				return true;
		}

		return false;

	}


	public List<Review> getReviews(String currentMail, String filter) {
		TypedQuery<Review> query = db.createQuery("SELECT r FROM Review r", Review.class);

		ArrayList<Review> ema = new ArrayList<Review>();

		for(Review r:query.getResultList()) {

			if(r.getSale().getSeller().getEmail().equals(currentMail) && r.getSale().getTitle().contains(filter)) 
				ema.add(r);

		}
		return ema;
	}

	public boolean hasReviewed(String currentUserMail, Sale sale) {
		db.getTransaction().begin();
		TypedQuery<Review> query = db.createQuery("SELECT r FROM Review r", Review.class);
		for(Review r : query.getResultList()) {
			if(r.getEvaluator().getEmail().equals(currentUserMail) && r.getSale().equals(sale))
				return true;
		}
		db.getTransaction().commit();	
		return false;
	}


	public void open(){
		String fileName=c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());

			emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);
			db = emf.createEntityManager();
		}
		System.out.println("DataAccess opened => isDatabaseLocal: "+c.isDatabaseLocal());


	}

	public BufferedImage getFile(String fileName) {
		File file=new File(basePath+fileName);
		BufferedImage targetImg=null;
		try {
			targetImg = rescale(ImageIO.read(file));
		} catch (IOException ex) {
			//Logger.getLogger(MainAppFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
		return targetImg;

	}

	public BufferedImage rescale(BufferedImage originalImage)
	{
		System.out.println("rescale "+originalImage);
		BufferedImage resizedImage = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
		g.dispose();
		return resizedImage;
	}



	public void close(){
		db.close();
		System.out.println("DataAcess closed");
	}


	public boolean isRegistered(String email) {
		TypedQuery<Registered> query = db.createQuery(
				"SELECT s FROM Registered s WHERE s.email = ?1",
				Registered.class
				);
		query.setParameter(1, email);
		return !query.getResultList().isEmpty();
	}


	public User isLogin(String email, String pass) {
		TypedQuery<User> query = db.createQuery(
				"SELECT u FROM User u WHERE u.email = ?1 AND u.password = ?2",
				User.class
				);

		query.setParameter(1, email);
		query.setParameter(2, pass);
		System.out.println(query.getResultList());
		return query.getResultList().isEmpty() ? null : query.getResultList().get(0);

	}



	public boolean removeSale(int saleNumber) {
		db.getTransaction().begin();
		Query query = db.createQuery("DELETE FROM Sale s WHERE s.saleNumber = ?1");
		query.setParameter(1, saleNumber);

		int deleted = query.executeUpdate();
		db.getTransaction().commit();

		if (deleted > 0) {
			System.out.println("Sale deleted correctly");
			return true;
		} else {
			System.out.println("No sale found with that number");
			return false;
		}
	}

	public boolean buySale(String mail, ArrayList<Integer> saleNumbers) throws NotEnoughMoneyException{
		db.getTransaction().begin();
		
		//Existitzen badira ==> Egoera aldatu + email-a balidatu + prezioa igo
		Registered buyer = db.find(Registered.class, mail);
		ArrayList<Sale> sales = new ArrayList<Sale>();
		Sale first = db.find(Sale.class, saleNumbers.get(0));
		if(first == null || buyer == null) {
			db.getTransaction().rollback();
			return false;
		}
		first.setSaleStatus(SaleStatusType.BOUGHT);
		sales.add(first);
		float totalPrize = first.getPrice();
		Registered seller = first.getSeller();

		int i = 1;
		while(i < saleNumbers.size()) {
			Sale s = db.find(Sale.class, saleNumbers.get(i));
			if(s == null || seller != s.getSeller()) {
				db.getTransaction().rollback();
				return false;
			}
			s.setSaleStatus(SaleStatusType.BOUGHT);
			sales.add(s);
			totalPrize += s.getPrice();
			i++;
		}

		if (totalPrize > buyer.getBalance()) {
			db.getTransaction().rollback();
			throw new NotEnoughMoneyException();
		}

		double newBuyerBalance = buyer.getBalance()-totalPrize;
		buyer.addToBought(sales);
		buyer.setBalance(newBuyerBalance);
		buyer.addToMovements(MovementType.BUY,totalPrize,newBuyerBalance,sales);


		double newSellerBalance = seller.getBalance()+totalPrize;
		seller.setBalance(newSellerBalance);
		seller.addToMovements(MovementType.SELL,totalPrize,newSellerBalance,sales);

		cleanWishLists(sales);

		db.getTransaction().commit();
		return true;

	}


	public void register(Registered seller) {
		db.getTransaction().begin();
		db.persist(seller);
		db.getTransaction().commit();
	}


	public Sale getSale(int saleNumber) {
		return db.find(Sale.class, saleNumber);
	}

	public Registered getRegistered(String email) {
		return db.find(Registered.class,email);
	}

	public boolean toggleWishList(String mail, int saleNumber) {

		boolean listanDago = isInWishList(mail, saleNumber);

		db.getTransaction().begin();
		Registered seller = db.find(Registered.class, mail);
		Sale sale = db.find(Sale.class, saleNumber);

		if (seller == null || sale == null) {
			db.getTransaction().commit();
			return false;
		}

		//Ezin daitezke bi trantsakzio aldi berean hasi, beraz hasieran ikusi dagoen edo ez
		if(!listanDago) seller.addToWishList(sale);
		else 			seller.removeFromWishList(sale);
		db.getTransaction().commit();
		return true;

	}

	public boolean isInWishList(String mail, int saleNumber) {
		db.getTransaction().begin();
		Registered seller = db.find(Registered.class, mail);
		Sale sale = db.find(Sale.class, saleNumber);

		if (seller == null || sale == null) {
			db.getTransaction().commit();
			return false;
		}
		db.getTransaction().commit();
		return seller.getWishList().contains(sale);


	}

	public void cleanWishLists(ArrayList<Sale> sales) {

		TypedQuery<Registered> query = db.createQuery("SELECT r FROM Registered r",Registered.class);

		List<Registered> users = query.getResultList();

		for (Registered r : users) {
			for(Sale sale:sales) {
				if (r.getWishList().contains(sale)) {
					r.removeFromWishList(sale);
				}
			}

		}

	}

	public Registered manageMoney(String rMail, double amount, MovementType type) throws NotEnoughMoneyException{
		db.getTransaction().begin();
		Registered reg = db.find(Registered.class, rMail);
		double balance = reg.getBalance();
		if(type == MovementType.WITHDRAW) {
			if(balance - amount < 0 ) throw new NotEnoughMoneyException();
			reg.setBalance(balance - amount);
			reg.addToMovements(type, amount, balance-amount,null); 
		}else if(type == MovementType.DEPOSIT ) {
			reg.setBalance(balance + amount);
			reg.addToMovements(type, amount,balance+amount,null); 
		}
		db.getTransaction().commit();
		return reg;
	}


	public void makeComplaint(String currentUserMail, int saleNumb, String complaint) {
		db.getTransaction().begin();
		Registered reg = db.find(Registered.class, currentUserMail);
		Sale s = db.find(Sale.class, saleNumb);

		for (Complaint c : reg.getComplaints()) {
			if (c.getSale().equals(s)) {
				db.getTransaction().rollback();
				return;
			}
		}
		reg.addComplaint(complaint,s);
		db.getTransaction().commit();
	}

	public boolean hasReported(String currentUsermail, Sale sale) {
		Registered reg = db.find(Registered.class, currentUsermail);
		Sale s = db.find(Sale.class, sale.getSaleNumber());
		for (Report r : reg.getReports()) {
			if (r.getSale().equals(s)) return true;
		}
		return false;
	}

	public void makeReport(String currentUsermail, int saleNumber, ReportReason reason) {
		db.getTransaction().begin();
		Registered reg = db.find(Registered.class, currentUsermail);
		Sale s = db.find(Sale.class, saleNumber);

		for (Report r : reg.getReports()) {
			if (r.getSale().equals(s)) {
				db.getTransaction().rollback();
				return;
			}
		}
		reg.addReport(reason,s);
		s.setSaleStatus(SaleStatusType.USER_REPORTED);
		db.getTransaction().commit();
	}

	public void declineReport(int reportID) {
		db.getTransaction().begin();

		Report r = db.find(Report.class, reportID);

		if (r == null) {
			db.getTransaction().commit();
			return;
		}

		Registered reg = r.getUser();
		Sale sale = r.getSale();

		if (reg != null) reg.getReports().remove(r);
		if (sale != null) sale.getReports().remove(r);
		db.remove(r);

		if (sale != null) {
			boolean reportsPending = false;
			for (Report rep : sale.getReports()) {
				if (!rep.isTreated()) {
					reportsPending = true;
					break;
				}
			}
			if (!reportsPending) {
				sale.setSaleStatus(SaleStatusType.ON_SALE);
			}
		}

		db.getTransaction().commit();
	}

	public void adminReport(int reportID) {
		db.getTransaction().begin();
		Report r = db.find(Report.class, reportID);
		Sale sale = r.getSale();
		sale.setSaleStatus(SaleStatusType.ADMIN_REPORTED);
		r.setTreated(true);
		db.getTransaction().commit();
	}

	public void declineComplaint(int complaintID) {
		db.getTransaction().begin();
		Complaint c = db.find(Complaint.class, complaintID);
		c.setTreated(true);
		db.getTransaction().commit();
	}

	public void acceptComplaint(int complaintID) {
		db.getTransaction().begin();
		Complaint c = db.find(Complaint.class, complaintID);
		c.setTreated(true);
		Sale sale = c.getSale();
		Registered buyer = c.getUser();
		Registered seller = sale.getSeller();

		buyer.getBought().remove(sale);

		double newBuyerBalance = buyer.getBalance()+sale.getPrice();
		buyer.setBalance(newBuyerBalance);
		ArrayList<Sale> s = new ArrayList<Sale>();
		s.add(sale);
		buyer.addToMovements(MovementType.REFUND_BUYER, sale.getPrice(), newBuyerBalance, s);

		double newSellerBalance = seller.getBalance()-sale.getPrice();
		seller.setBalance(newSellerBalance);
		seller.addToMovements(MovementType.REFUND_SELLER,sale.getPrice(), newSellerBalance,s);

		db.getTransaction().commit();
	}


	public void createRequest(String mail,String title,String description,double price) {
		db.getTransaction().begin();
		Registered reg = db.find(Registered.class,mail);
		reg.addRequest(title, description, price);
		db.getTransaction().commit();
	}

	public void makeOffer(String offererMail, Request request, double price, int status, String description) {
		db.getTransaction().begin();
		Registered reg = db.find(Registered.class,offererMail);
		if (reg== null) return;
		reg.addOffer(request,status,price,description);
		db.getTransaction().commit();
	}

	public void acceptOffer(Offer offer) throws NotEnoughMoneyException{
		db.getTransaction().begin();

		Offer o = db.find(Offer.class, offer.getOfferId());
		Request r = o.getRequest();

		o.setOfferStatus(OfferStatusType.ACCEPTED);
		r.setRequestStatus(RequestStatusType.COMPLETED);

		String offererEmail = o.getRegistered().getEmail();
		String requesterEmail = r.getRequester().getEmail();

		Registered requester = db.find(Registered.class, requesterEmail);
		Registered offerer = db.find(Registered.class, offererEmail);

		Double price = o.getPrice();

		if (price > requester.getBalance()) {
			throw new NotEnoughMoneyException();
		}

		Sale s = offerer.addSale(r.getTitle(), r.getDescription(), o.getStatus(), (float) o.getPrice(), UtilDate.trim(new Date()), null);
		s.setSaleStatus(SaleStatusType.BOUGHT);

		ArrayList<Sale> saleList = new ArrayList<>();
		saleList.add(s);

		double newBuyerBalance  = requester.getBalance() - price;
		double newSellerBalance = offerer.getBalance()   + price;

		requester.addToBought(saleList);
		requester.setBalance(newBuyerBalance);
		requester.addToMovements(MovementType.BUY, price, newBuyerBalance, saleList);

		offerer.setBalance(newSellerBalance);
		offerer.addToMovements(MovementType.SELL, price, newSellerBalance, saleList);

		db.getTransaction().commit();

	}

	public void declineOffer(Offer offer) {
		db.getTransaction().begin();
		Offer o = db.find(Offer.class, offer.getOfferId());
		o.setOfferStatus(OfferStatusType.DECLINED);
		db.getTransaction().commit();

	}

	public void makeReview(String currentUserMail, Sale sale, int rating, String desc) {
		db.getTransaction().begin();
		Registered buyer = db.find(Registered.class,currentUserMail);
		Registered seller = db.find(Registered.class,sale.getSeller().getEmail());

		seller.addReview(rating, desc, sale, buyer);

		int sum = 0;
		for(Review r : seller.getReviews()) {
			if (r.getSale().getSeller().equals(seller)) {
				sum += r.getRating();
			}
		}

		Double newRating = (double) sum / seller.getReviews().size();
		seller.setRating(newRating);

		db.getTransaction().commit();
	}







}
