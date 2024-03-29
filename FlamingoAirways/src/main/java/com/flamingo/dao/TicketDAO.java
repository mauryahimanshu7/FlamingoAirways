package com.flamingo.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.flamingo.exception.AdException;
import com.flamingo.pojo.Airplane;
import com.flamingo.pojo.FlightDetails;
import com.flamingo.pojo.Passenger;
import com.flamingo.pojo.Ticket;

public class TicketDAO extends DAO{

	public Ticket bookTicket(Passenger passengerDetails, FlightDetails flightDetails) throws AdException{
		
		
		try {
            begin();
            Ticket ticket = new Ticket(passengerDetails, flightDetails);   
            getSession().save(ticket);
            commit();
            return ticket;
            
        } catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create flight", e);
            throw new AdException("Exception while creating ticket: " + e.getMessage());
        }  finally{
			close();
		}      

		
		
	}
	
	public void cancelTicket(Passenger passengerDetails,FlightDetails flightDetails) throws AdException
	{
		try{
			begin();
			long passenger_id = passengerDetails.getId();
			long flight_id = flightDetails.getFlight_id();
			Query q = getSession().createQuery("From Ticket where passenger_id=:passenger_id and flight_id=:flight_id");
			q.setLong("passenger_id",passenger_id);
			q.setLong("flight_id",flight_id);
			Ticket ticket = (Ticket)q.uniqueResult();
			getSession().delete(ticket);
			
			
			int oldAvail = flightDetails.getAvailableSeats();
			flightDetails.setAvailableSeats(oldAvail+1);
			getSession().update(flightDetails);
			
			commit();
		}
		catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create flight", e);
            throw new AdException("Exception while deleting ticket: " + e.getMessage());
        } 
		finally{
			close();
		}
	}
	
	public void deleteTickets(long flight_id) throws AdException
	{
		try{
			begin();
			Query q = getSession().createQuery("From Ticket where flight_id=:flight_id");
			q.setLong("flight_id",flight_id);	
			List <Ticket>list = q.list();
			commit();
			//Iterate to delete all ticket objects
			
			for(Ticket t:list)
			{
				begin();
				getSession().delete(t);
				commit();
			}
			
		}
		catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create flight", e);
            throw new AdException("Exception while deleting ticket: " + e.getMessage());
        }   
		finally{
			close();
		}
	}
	
	public List TicketList()
	{
		List<Ticket>ticketList= new ArrayList<Ticket>();
		try{
		begin();
		Query q = getSession().createQuery("From Ticket");
		ticketList = q.list();
		commit();
		
		}
		catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create flight", e);
            System.out.println("Exception while listing ticket: " + e.getMessage());
        }  
		finally{
			close();
		}
		return ticketList;
		
	}
	
}
