package net.javaguides.login.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.javaguides.login.model.LoginModel;
import net.javaguides.login.dao.LoginDao;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginDao loginDao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		loginDao = new LoginDao();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/insert":
				insertLogin(request, response);
				break;
			case "/select":
				selectLogin(request, response);
				break;
			default:
				listLogin(request, response);
				break;
			}
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	private void listLogin(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<LoginModel> listUser = loginDao.selectAllLogin();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}

	private void selectLogin(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		boolean isValidUser = loginDao.checkLogin(email, password);

		if (isValidUser) {
			response.sendRedirect("loginsuccess.jsp");
		} else {
			try {
				request.setAttribute("errorMessage", "Sai email hoặc mật khẩu!");
				RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
	}

	private void insertLogin(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		if (loginDao.isEmailExists(email)) {
			request.setAttribute("errorMessage", "Email đã được đăng ký. Vui lòng dùng email khác.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("signup.jsp");
			dispatcher.forward(request, response);
			return;
		}

		LoginModel newLogin = new LoginModel(username, email, password);
		loginDao.insertLogin(newLogin);
		response.sendRedirect("login.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
