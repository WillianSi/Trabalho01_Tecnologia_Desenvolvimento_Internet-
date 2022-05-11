package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Company;
import model.ModelException;
import model.Produto;
import model.User;
import model.dao.CompanyDAO;
import model.dao.DAOFactory;
import model.dao.ProdutoDAO;
import model.dao.UserDAO;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/produtos", "/produto/form", "/produto/delete", "/produto/insert", "/produto/update"})
public class ProdutosController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
		throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/post-manager/produto/form": {
			CommonsController.listCompany(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-produto.jsp");
			break;
		}
		case "/post-manager/produto/update":{
			CommonsController.listCompany(req);
			req.setAttribute("action", "update");
			Produto p = loadProduto(req);
			req.setAttribute("produto", p);
			ControllerUtil.forward(req, resp, "/form-produto.jsp");
			break;
		}
		default:
			listProdutos(req);
			
			ControllerUtil.transferSessionMessagesToRequest(req);
			
			ControllerUtil.forward(req, resp, "/produtos.jsp");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/post-manager/produto/insert": {
			insertProduto(req, resp);
			break;
		}
		case "/post-manager/produto/update":{
			updateProduto(req, resp);
			break;
		}
		case "/post-manager/produto/delete":{
			deleteProduto(req, resp);
			break;
		}
		default:
			System.out.println("URL inválida " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/produtos");
	}

	private void insertProduto(HttpServletRequest req, HttpServletResponse resp) {
		
		String produtoName = req.getParameter("name");
		String fabrica = req.getParameter("fabrica");
		String validade = req.getParameter("validade");
		Integer companyId = Integer.parseInt(req.getParameter("company"));
		
		Produto produto = new Produto();
		produto.setName(produtoName);
		
		Date fabriDate = formatDate(fabrica);
		produto.setFabrica(fabriDate);
		
		Date valiDate = formatDate(validade);
		produto.setValidade(valiDate);

		Company company = new Company(companyId);
		produto.setCompany(company);

		ProdutoDAO dao = DAOFactory.createDAO(ProdutoDAO.class);

		try {
			if (dao.save(produto)) {
				ControllerUtil.sucessMessage(req, 
						"Produto '" + produto.getName() + "' salvo com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, 
						"Produto '" + company.getName() + "' não pode ser salvo.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private Date formatDate(String stringDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(stringDate);
		} catch (ParseException pe) {
			return null;
		}
	}

	private void listProdutos(HttpServletRequest req) {
		ProdutoDAO dao = DAOFactory.createDAO(ProdutoDAO.class);
		
		List<Produto> produtos = null;
		
		try {
			produtos = dao.listAll();
		} catch (ModelException e) {
			
			e.printStackTrace();
		}
		
		if (produtos != null)
			req.setAttribute("produtos", produtos);
	}
	
	private void deleteProduto(HttpServletRequest req, HttpServletResponse resp) {
		String produtoIdParameter = req.getParameter("id");
		
		int produtoId = Integer.parseInt(produtoIdParameter);
		
		ProdutoDAO dao = DAOFactory.createDAO(ProdutoDAO.class);
		
		try {
			Produto produto = dao.findById(produtoId);
			
			if (produto == null)
				throw new ModelException("Empresa não encontrada para deleção.");
			
			if (dao.delete(produto)) {
				ControllerUtil.sucessMessage(req, "Produto '" + produto.getName() + "' deletado com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Produto '" + produto.getName() + "' não pode ser deletadao.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
	
	private Produto loadProduto(HttpServletRequest req) {
		String produtoIdParameter = req.getParameter("produtoId");
		
		int produtoId = Integer.parseInt(produtoIdParameter);
		
		ProdutoDAO dao = DAOFactory.createDAO(ProdutoDAO.class);
		
		try {
			Produto p = dao.findById(produtoId);
			
			if (p == null)
				throw new ModelException("Empresa n�o encontrada para alteras�o");
			
			return p;
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
		return null;
	}
	
	private void updateProduto(HttpServletRequest req, HttpServletResponse resp) {
	String produtoName = req.getParameter("name");
	String start = req.getParameter("fabrica");
	String end = req.getParameter("validade");
	Integer userId = Integer.parseInt(req.getParameter("company"));
	
	Produto produto = loadProduto(req);
	produto.setName(produtoName);
	
	Date startDate = formatDate(start);
	produto.setValidade(startDate);

	Date endDate = formatDate(end);
	produto.setValidade(endDate);
	
	Company company = new Company(userId);
	produto.setCompany(company);
	
	ProdutoDAO dao = DAOFactory.createDAO(ProdutoDAO.class);
	
	try {
		if (dao.update(produto)) {
			ControllerUtil.sucessMessage(req, "Produto '" + company.getName() + "' atualizado com sucesso.");
		}
		else {
			ControllerUtil.errorMessage(req, "Produto '" + company.getName() + "' não pode ser atualizado.");
		}				
	} catch (ModelException e) {
		// log no servidor
		e.printStackTrace();
		ControllerUtil.errorMessage(req, e.getMessage());
	}		
	}
}
