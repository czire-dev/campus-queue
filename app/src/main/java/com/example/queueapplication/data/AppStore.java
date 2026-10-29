package com.example.queueapplication.data;

import android.content.Context;
import android.util.Log;

import com.example.queueapplication.R;
import com.example.queueapplication.data.enums.Priority;
import com.example.queueapplication.data.enums.Role;
import com.example.queueapplication.data.enums.Status;
import com.example.queueapplication.data.models.Service;
import com.example.queueapplication.data.models.Ticket;
import com.example.queueapplication.data.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AppStore {
    private static AppStore instance;
    public List<Service> services = new ArrayList<>();
    public List<Ticket> tickets = new ArrayList<>();
    public List<User> users = new ArrayList<>();
    public List<Ticket> history = new ArrayList<>();

    private int serviceId = 0;
    private int ticketId = 0;
    private int userId = 0;
    private int historyId = 0;

    private User currentUser = null;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private Service currentService = null;

    public Service getCurrentService() {
        return currentService;
    }

    public void setCurrentService(Service currentService) {
        this.currentService = currentService;
    }

    private AppStore(Context context) {
        seedData();
    }

    private void seedData() {
        Service reg = new Service(serviceId++,R.drawable.baseline_how_to_reg_24, "Registrar", "Academic, Records", "REG", Priority.MEDIUM);
        Service cs = new Service(serviceId++,R.drawable.baseline_monetization_on_24, "Cashier", "Payment & Fees", "CS", Priority.MEDIUM);
        Service it = new Service(serviceId++,R.drawable.baseline_computer_24, "IT Help Desk", "Technical Support", "IT", Priority.MEDIUM);
        Service sch = new Service(serviceId++,R.drawable.baseline_school_24, "Scholarship Office", "Financial Aid", "SCH", Priority.HIGH);

        services.add(reg);
        services.add(cs);
        services.add(it);
        services.add(sch);

        User s1 = new User(userId++,"Student 1", Role.STUDENT);
        User s2 = new User(userId++,"Student 2", Role.STUDENT);
        User s3 = new User(userId++,"Student 3", Role.STUDENT);
        User s4 = new User(userId++,"Student 4", Role.STUDENT);

        User s5 = new User(userId++,"Staff 1", Role.STAFF);
        User s6 = new User(userId++,"Staff 2", Role.STAFF);
        User s7 = new User(userId++,"Staff 3", Role.STAFF);
        User s8 = new User(userId++,"Staff 4", Role.STAFF);

        users.addAll(Arrays.asList(s1,s2,s3,s4,s5,s6,s7,s8));

        Ticket t1 = new Ticket(ticketId++,"REG-001", services.get(0), s1);
        Ticket t2 = new Ticket(ticketId++,"CS-001", services.get(1),s2);
        Ticket t3 = new Ticket(ticketId++,"IT-001", services.get(2),s3);
        Ticket t4 = new Ticket(ticketId++,"SCH-001", services.get(3),s4);
        t1.setStatus(Status.SERVING);

        tickets.addAll(Arrays.asList(t1, t2, t3, t4));

        setCurrentUser(s1);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public List<Ticket> getSortedWaitingList() {
        return tickets.stream().filter(ticket -> ticket.getStatus() == Status.WAITING)
                .sorted(
                        Comparator.comparing((Ticket t) -> t.getService().getPriorityLevel())
                                .thenComparingLong(Ticket::getDateCreated)
                )
                .collect(Collectors.toList());
    }

    public List<Ticket> getCurrentUserHistory() {
        return history.stream().filter(ticket -> ticket.getUser().getId() == currentUser.getId()).collect(Collectors.toList());
    }

    public Ticket getCurrentServingByService(Service service) {
        List<Ticket> ticketList = tickets.stream().filter(ticket -> ticket.getService().getId() == service.getId() && ticket.getStatus() == Status.SERVING).collect(Collectors.toList());
        return ticketList.isEmpty() ? null : ticketList.get(0);
    }

    public List<Ticket> getTicketsByService(Service service) {
        if(service == null) return null;

        List<Ticket> output = new ArrayList<>();
        for(Ticket ticket : getSortedWaitingList()) {
            if(ticket.getService().getId() == service.getId()) {
                output.add(ticket);
            }
        }
        return output;
    }

    private int getTicketNumber() {
        List<Ticket> waitingList = getTicketsByService(currentService).stream().filter(ticket -> ticket.getStatus() == Status.WAITING || ticket.getStatus() == Status.SERVING).collect(Collectors.toList());
        return waitingList.size() + 1;
    }

    public Ticket getActiveTicket() {
        for(Ticket ticket : tickets) {
            if(ticket.getUser().getId() == currentUser.getId() && (ticket.getStatus() == Status.WAITING ||  ticket.getStatus() == Status.SERVING)) {
                return ticket;
            }
        }
        return null;
    }

    public int getPosition(Ticket ticket) {
        List<Ticket> ticketList = getTicketsByService(ticket.getService());
        int index = ticketList.indexOf(ticket);
        if(index == -1) return 0;

        Ticket serving = getCurrentServingByService(ticket.getService());
        return serving != null ? index + 2 : index + 1;
    }

    public Ticket joinQueue() {
        if(getActiveTicket() != null) {
            return null;
        }

        Ticket newTicket = new Ticket(ticketId++,"", currentService, currentUser);
        newTicket.setTicketNo(currentService.getPrefix() + "-00" + getTicketNumber());
        tickets.add(newTicket);

        return newTicket;
    }

    public Ticket changeStatus(Ticket ticket, Status newStatus) {
        if(!isValidStatusChange(ticket.getStatus(), newStatus)) {
            return null;
        }

        ticket.setStatus(newStatus);

        if(newStatus == Status.CANCELLED || newStatus == Status.COMPLETED) {
            history.add(ticket);
            tickets.removeIf(ticket1 -> ticket1.getId() == ticket.getId());
            Log.d("HISTORY_SIZE", history.size() +"");
            Log.d("TICKETS_SIZE", tickets.size()+"");
            return ticket;
        }

        for (int i = 0; i < tickets.size(); i++) {
            if(tickets.get(i).getId() == ticket.getId()) {
                tickets.set(i, ticket);
                return ticket;
            }
        }

        Log.d("HERE", "NULL");
        return null;
    }

    private boolean isValidStatusChange(Status oldStatus, Status newStatus) {
        switch(oldStatus) {
            case WAITING:
                return newStatus == Status.CANCELLED || newStatus == Status.SERVING;
            case SERVING:
                return newStatus == Status.COMPLETED;
            case CANCELLED:
            case COMPLETED:
            default:
                return false;
        }
    }

    public static synchronized void init(Context context) {
        if(instance == null) instance = new AppStore(context);
    }
    public static AppStore getInstance() {
        if(instance == null) throw new IllegalStateException("AppStore must be initialized");
        return instance;
    }
}
