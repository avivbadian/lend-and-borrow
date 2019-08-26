package nimpash.cinema.utils;

import nimpash.cinema.objects.PurchaseDetails;
import nimpash.cinema.objects.Screening;
import nimpash.cinema.objects.Seat;
import nimpash.cinema.objects.MovieDetails;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class EmailService
{

    private static final String MAIL_ADDRESS = "nimpashcinema@gmail.com";
    private static final String MAIL_PASS = "nimpashcinema123456789";
    private static final String AUTH = "mail.smtp.auth";
    private static final String STARTTLS = "mail.smtp.starttls.enable";
    private static final String HOST = "mail.smtp.host";
    private static final String HOST_GMAIL = "smtp.gmail.com";
    private static final String PORT = "mail.smtp.port";
    private static final String PORT_VALUE = "587";

    private final Properties _properties;
    private final static EmailService _sendMail = new EmailService();

    private EmailService()
    {
        _properties = new Properties();
        _properties.put(AUTH, "true");
        _properties.put(STARTTLS, "true");
        _properties.put(HOST, HOST_GMAIL);
        _properties.put(PORT, PORT_VALUE);
    }

    public static EmailService GetInstance()
    {
        return _sendMail;
    }

    public synchronized void SendMail(PurchaseDetails purchase, MovieDetails movie, Screening screening)
    {

        Session session = Session.getInstance(_properties,
                                              new javax.mail.Authenticator() {
                                                  protected PasswordAuthentication getPasswordAuthentication() {
                                                      return new PasswordAuthentication(MAIL_ADDRESS, MAIL_PASS);
                                                  }
                                              });

        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL_ADDRESS)); //from  same email id
            message.setRecipients(Message.RecipientType.TO,
                                  InternetAddress.parse(purchase.Email));//TO  whome u have to send mails that person id

            message.setSubject("NimPashCinema Order: " + purchase.Id);
            message.setText(new StringBuilder()
                                    .append("Dear ")
                                    .append(purchase.GivenName)
                                    .append(" ")
                                    .append(purchase.LastName)
                                    .append(",\n\n")
                                    .append("Your Order Number: ").append(purchase.Id).append("\n\n")
                                    .append("Your Order Details: \n\n").append(ConstructPurchaseSummery(movie, screening, purchase.Seats))
                                    .append("Thank you for purchasing a tickets from NimPashCinema")
                                    .append("\n\n").append(" Enjoy the Screening ! ").toString()

            );

            Transport.send(message);

            System.out.println("Done");
        }
        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }


    private String ConstructPurchaseSummery(MovieDetails movie, Screening scr, List<Seat> selectedSeats)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Movie name: ").append(movie.Name).append(" (" + movie.Duration + " minutes) ").append("\n\n").append("Hall ").append(scr.HallId)
                .append(" at ").append(scr.Time.toString(" dd/MM, HH:mm ")).append("\n\n").append("Seats: ");

        builder.append(selectedSeats
                               .parallelStream()
                               .map(seat -> "Row " +
                                       seat.RowNumber +
                                       " Seat " +
                                       seat.SeatNumber)
                               .collect(Collectors.joining(", ")));

        builder.append("\n\n").append("Total Cost: ").append(scr.Price  + "\n\n\n");
        return builder.toString();
    }
}

