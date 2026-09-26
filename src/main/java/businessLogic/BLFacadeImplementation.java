package businessLogic;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.objectdb.o.CLN.p;

import dataAccess.DataAccess;
import domain.Sale;
import domain.User;
import enums.MovementType;
import enums.ReportReason;
import enums.SaleType;
import domain.Complaint;
import domain.ComplaintContainer;
import domain.Movement;
import domain.MovementContainer;
import domain.Offer;
import domain.OfferContainer;
import domain.Registered;
import domain.Report;
import domain.ReportContainer;
import domain.Request;
import domain.RequestContainer;
import domain.Review;
import domain.ReviewContainer;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.NotEnoughMoneyException;
import exceptions.SaleAlreadyExistException;

import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	
    private static final Logger logger = LoggerFactory.getLogger(BLFacadeImplementation.class);
	
	private static final int BASE_SIZE = 160;

	private static final String BASE_PATH="src/main/resources/images/";
	DataAccess dbManager;

	public BLFacadeImplementation()  {
		System.out.println("Creating BLFacadeImplementation instance");
		dbManager=new DataAccess();		
	}

	public BLFacadeImplementation(DataAccess da)  {
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		dbManager=da;		
	}


	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public void createSale(String title, String description,int status, float price, Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
		dbManager.open();
		Sale product=dbManager.createSale(title, description, status, price, pubDate, sellerEmail, file);		
		dbManager.close();
		//return product;
	};

	/**
	 * {@inheritDoc}
	 */
	/*
	@WebMethod 
	public List<Sale> getSales(String desc){
		dbManager.open();
		List<Sale>  rides=dbManager.getSales(desc);
		dbManager.close();
		return rides;
	}
	 */
	/**
	 * {@inheritDoc}
	 */
	@WebMethod 
	public List<Sale> getQuery(String desc, Date pubDate, SaleType query, String email, String sellerEmail, ArrayList<Sale> basket) {
		System.out.println(query);
		dbManager.open();
		System.out.println(query);

		List<Sale> rides= new ArrayList<Sale>();
		switch (query) {
		case ON_SALES:
			rides = dbManager.getOnSales(email, desc);
			break;
		case PUBLISHED_SALES:
			rides = (sellerEmail.equals("")) ? dbManager.getPublishedSales(desc,pubDate,email) : dbManager.getPublishedSales(desc,pubDate,email, sellerEmail, basket);
			break;
		case PURCHASED:
			rides = dbManager.getPurchased(email, desc);
			break;
		case WISHLIST:
			rides = dbManager.getWhisList(email, desc);
			break;			
		}
		dbManager.close();
		return rides;
	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod 
	public List<MovementContainer> getMovements(String email, MovementType type) {
		dbManager.open();
		List<Movement> rides= dbManager.getMovements(email, type);
		List<MovementContainer> res = new ArrayList<>();

		for (Movement c : rides) {
			res.add(new MovementContainer(c));
		}
		dbManager.close();
		return res;
	}

	public List<ComplaintContainer> getComplaints() {
		dbManager.open();
		List<Complaint> rides= dbManager.getComplaints();;
		List<ComplaintContainer> com = new ArrayList<>();

		for (Complaint c : rides) {
			com.add(new ComplaintContainer(c));
		}

		dbManager.close();
		return com;
	}

	public List<ReportContainer> getReports() {
		dbManager.open();

		List<Report> reports = dbManager.getReports();
		List<ReportContainer> res = new ArrayList<>();

		for (Report r : reports) {
			res.add(new ReportContainer(r));
		}

		dbManager.close();
		return res;
	}



	@WebMethod public List<RequestContainer> getRequests(String currentMail){
		dbManager.open();

		List<Request> requests = dbManager.getRequests(currentMail);
		List<RequestContainer> req = new ArrayList<>();

		for (Request r : requests) {
			req.add(new RequestContainer(r));
		}

		dbManager.close();
		return req;
	}


	@WebMethod public List<OfferContainer> getOffers(String currentMail,String filter){
		dbManager.open();

		List<Offer> offers = dbManager.getOffers(currentMail,filter);
		List<OfferContainer> ofs = new ArrayList<>();

		for (Offer o : offers) {
			ofs.add(new OfferContainer(o));
		}

		dbManager.close();
		return ofs;
	}
	
	@WebMethod public List<ReviewContainer> getReviews(String currentMail, String filter){
		dbManager.open();
		List<Review> reviews = dbManager.getReviews(currentMail, filter);
		List<ReviewContainer> rvs= new ArrayList<>();
		
		for(Review r: reviews) {
			rvs.add(new ReviewContainer(r));
		}
		dbManager.close();
		
		return rvs;


	}


	/**
	 * {@inheritDoc}
	 */
	@WebMethod public BufferedImage getFile(String fileName) {
		return dbManager.getFile(fileName);
	}


	public void close() {
		DataAccess dB4oManager=new DataAccess();
		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod	
	public void initializeBD(){
		dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}
	/**
	 * {@inheritDoc}
	 */
	@WebMethod public Image downloadImage(String imageName) {
		File image = new File(BASE_PATH+imageName);
		try {
			return ImageIO.read(image);
		} catch (IOException e) {
			logger.error("context", e);
		}
		return null;
	}

	@WebMethod public boolean isRegistered(String mail){
		dbManager.open();
		boolean b = dbManager.isRegistered(mail);
		dbManager.close();
		return b;
	}

	@WebMethod public User isLogin(String mail, String pass){
		dbManager.open();
		User a = dbManager.isLogin(mail,pass);
		dbManager.close();
		return a;
	}
	@WebMethod public void register(Registered seller) {
		dbManager.open();
		dbManager.register(seller);
		dbManager.close();
	}


	@WebMethod public Sale getSale(int saleNumber) {
		dbManager.open();
		Sale s = dbManager.getSale(saleNumber);
		dbManager.close();
		return s;
	}

	public Registered getRegistered(String email) {
		dbManager.open();
		Registered reg = dbManager.getRegistered(email);
		dbManager.close();
		return reg;
	}

	@WebMethod public boolean removeSale(int SaleNumber) {
		dbManager.open();
		boolean b = dbManager.removeSale(SaleNumber);
		dbManager.close();
		return b;
	}

	@WebMethod public boolean buySale(String mail, ArrayList<Integer> saleNumbers) throws NotEnoughMoneyException{
		dbManager.open();
		boolean b = dbManager.buySale(mail, saleNumbers);
		dbManager.close();
		return b;
	}

	@WebMethod public boolean toggleWishList(String mail, int saleNumber) {
		dbManager.open();
		boolean result = dbManager.toggleWishList(mail, saleNumber);
		dbManager.close();
		return result;
	}

	@WebMethod public boolean isInWishList(String mail, int saleNumber) {
		dbManager.open();
		boolean result = dbManager.isInWishList(mail, saleNumber);
		dbManager.close();
		return result;
	}



	@WebMethod public Registered manageMoney(String rMail,double amount, MovementType type) throws NotEnoughMoneyException {
		dbManager.open();
		Registered result = dbManager.manageMoney(rMail, amount, type);
		dbManager.close();
		return result;
	}

	@WebMethod public void makeComplaint(String currentUsermail, int saleNumb, String complaint) {
		dbManager.open();
		dbManager.makeComplaint(currentUsermail,saleNumb,complaint);
		dbManager.close();
	}

	@WebMethod public boolean hasReported(String currentUsermail, Sale sale) {
		dbManager.open();
		boolean result = dbManager.hasReported(currentUsermail, sale);
		dbManager.close();
		return result;
	}


	public void makeReport(String currentUsermail, int saleNum, ReportReason reason) {
		dbManager.open();
		dbManager.makeReport(currentUsermail,saleNum,reason);
		dbManager.close();
	}

	public void declineReport(int reportID) {
		dbManager.open();
		dbManager.declineReport(reportID);
		dbManager.close();
	}

	public void adminReport(int reportID) {
		dbManager.open();
		dbManager.adminReport(reportID);
		dbManager.close();
	}

	public void declineComplaint(int complaintID) {
		dbManager.open();
		dbManager.declineComplaint(complaintID);
		dbManager.close();
	}


	public void acceptComplaint(int complaintID) {
		dbManager.open();
		dbManager.acceptComplaint(complaintID);
		dbManager.close();
	}


	public void createRequest(String mail,String title,String description,double price) {
		dbManager.open();
		dbManager.createRequest(mail,title,description,price);
		dbManager.close();
	}


	public void makeOffer(String offererMail, Request request, double price, int status, String description) {
		dbManager.open();
		dbManager.makeOffer(offererMail, request, price, status, description);
		dbManager.close();

	}


	public void acceptOffer(Offer offer) throws NotEnoughMoneyException {
		dbManager.open();
		dbManager.acceptOffer(offer);
		dbManager.close();

	}

	public void declineOffer(Offer offer) {
		dbManager.open();
		dbManager.declineOffer(offer);
		dbManager.close();

	}

	public boolean hasOffer(Request request, String currentUserMail) {
		dbManager.open();
		boolean b = dbManager.hasOffer(request,currentUserMail);
		dbManager.close();
		return b;
	}

	public void makeReview(String currentUserMail, Sale sale, int rating, String desc) {
		dbManager.open();
		dbManager.makeReview(currentUserMail,sale,rating,desc);
		dbManager.close();
	}

	public boolean hasReviewed(String currentUserMail, Sale s) {
		dbManager.open();
		boolean b = dbManager.hasReviewed(currentUserMail, s);
		dbManager.close();
		return b;
	}


}

