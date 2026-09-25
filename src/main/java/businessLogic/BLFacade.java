package businessLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.awt.image.BufferedImage;
import java.awt.Image;

import gui.*;
/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade  {
	  

	/**
	 * This method creates/adds a product to a seller
	 * 
	 * @param title of the product
	 * @param description of the product
	 * @param status 
	 * @param selling price
	 * @param category of a product
	 * @param publicationDate
	 * @return Sale
	 */
   @WebMethod
	public void createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException;
	
	
	/**
	 * This method retrieves the products that contain desc
	 * 
	 * @param desc the text to search
	 * @return collection of sales that contain desc 
	 */
	//@WebMethod public List<Sale> getSales(String desc);
	
	/**
	 * 	 * This method retrieves the products that contain a desc text in a title and the publicationDate today or before
	 * 
	 * @param desc the text to search
	 * @param pubDate the date  of the publication date
	 * @param sellerEmail 
	 * @param basket 
	 * @return collection of sales that contain desc and published before pubDate
	 */
	@WebMethod public List<Sale> getQuery(String desc, Date pubDate, SaleType query, String email, String sellerEmail, ArrayList<Sale> basket);
	
	@WebMethod public List<MovementContainer> getMovements(String email, MovementType type);

	@WebMethod public List<ComplaintContainer> getComplaints();
	
	@WebMethod public List<ReportContainer> getReports();
	
	@WebMethod public List<RequestContainer> getRequests(String currentMail);
	
	@WebMethod 	public List<OfferContainer> getOffers(String currentMail, String filter);

	//@WebMethod public List<Sale> getReports(String titleFilter);
	
	@WebMethod public List<ReviewContainer> getReviews(String currentMail, String filter);

	
	
	/**
	 * This method calls the data access to initialize the database with some sellers and products.
	 * It is only invoked  when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod public void initializeBD();
	
	@WebMethod public Image downloadImage(String imageName);
	
	@WebMethod public boolean isRegistered(String mail);
		
	@WebMethod public User isLogin(String mail, String pass);

	@WebMethod public void register(Registered seller);
	
	@WebMethod public Sale getSale(int saleNumber);
	
	@WebMethod public Registered getRegistered(String email);

	@WebMethod public boolean removeSale(int SaleNumber);
	
	@WebMethod public boolean buySale(String mail, ArrayList<Integer> saleNumbers) throws NotEnoughMoneyException;
	
	@WebMethod public boolean toggleWishList(String mail, int saleNumber);

	@WebMethod public boolean isInWishList(String mail, int saleNumber);
	
	@WebMethod public Registered manageMoney(String rMail, double amount, MovementType type) throws NotEnoughMoneyException;
	
	@WebMethod public void makeComplaint(String currentUsermail, int saleNumb, String complaint);
	
	@WebMethod public boolean hasReported(String currentUsermail, Sale sale);
	
	@WebMethod public void makeReport(String currentUsermail, int saleNum, ReportReason reason);

	@WebMethod public void declineReport(int reportID);
	
	@WebMethod public void adminReport(int reportID);
	
	@WebMethod public void declineComplaint(int complaintID);
	
	@WebMethod public void acceptComplaint(int complaintID);
	
	@WebMethod public void createRequest(String mail,String title,String description,double price);

	@WebMethod public void makeOffer(String offererMail, Request request, double price, int status, String description);

	@WebMethod public void acceptOffer(Offer offer)  throws NotEnoughMoneyException;
	
	@WebMethod public void declineOffer(Offer offer);

	@WebMethod public boolean hasOffer(Request request, String currentUserMail);

	@WebMethod public void makeReview(String currentUserMail, Sale sale, int rating, String desc);

	@WebMethod public boolean hasReviewed(String currentUserMail, Sale s);
	
}
