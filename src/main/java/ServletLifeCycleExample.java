import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


public class ServletLifeCycleExample extends HttpServlet {
    private Integer sharedCounter;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        getServletContext().log("init() called");
        sharedCounter = 0;
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        getServletContext().log("service() called");
        int localCounter;
        synchronized (sharedCounter) {
            sharedCounter++;
            localCounter = sharedCounter;
        }
        response.getWriter().write("Incrementing the count to " + localCounter);  // accessing a local variable
        super.service(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        writer.println("<html><title>Welcome</title><body>");
        writer.println("<h1>Have a Great Day!</h1>");
        writer.println("</body></html>");
    }
    @Override
    public void destroy() {
        getServletContext().log("destroy() called");
    }
}