package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Company;
import model.ModelException;
import model.Produto;
import model.User;

public class MySQLProdutoDAO implements ProdutoDAO {

	@Override
	public Produto findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sql = "SELECT * FROM produtos WHERE id = ?;";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Produto p = null;
		while (db.next()) {
			p = new Produto(id);
			p.setName(db.getString("nameProduto"));
			p.setFabrica(db.getDate("fabri"));
			p.setValidade(db.getDate("vali"));
			
			CompanyDAO companyDAO = DAOFactory.createDAO(CompanyDAO.class); 
			Company company = companyDAO.findById(db.getInt("companies_id"));
			p.setCompany(company);
			
			break;
		}
		
		return p;
	}
	
	@Override
	public List<Produto> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Produto> produtos = new ArrayList<Produto>();
		
		// Declara uma instrução SQL
		String sqlQuery = "SELECT c.id as 'produtos_id', c.*, u.* FROM produtos c  INNER JOIN companies u ON c.companies_id = u.id;"; 
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			
			Company company = new Company(db.getInt("companies_id"));
			company.setName(db.getString("name"));
			company.setRole(db.getString("role"));
			company.setStart(db.getDate("start"));
			company.setEnd(db.getDate("end"));
			
			Produto produto = new Produto(db.getInt("produtos_id"));
			produto.setName(db.getString("nameProduto"));
			produto.setFabrica(db.getDate("fabri"));
			produto.setValidade(db.getDate("vali"));
			produto.setCompany(company);
			
			produtos.add(produto);
		}
		
		return produtos;
	}

	@Override
	public boolean save(Produto produto) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO produtos VALUES (DEFAULT, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		
		db.setString(1, produto.getName());
		db.setDate(2, produto.getFabrica() == null ? new Date() : produto.getFabrica());
			
		if (produto.getValidade() == null)
			db.setNullDate(3);
		else db.setDate(3, produto.getValidade());

		db.setInt(4, produto.getCompany().getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(Produto produto) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE produtos "
				+ " SET nameProduto = ?, "
				+ " fabri = ?, "
				+ " vali = ?, "
				+ " companies_id = ? "
				+ " WHERE id = ?; "; 
		
		db.prepareStatement(sqlUpdate);
		
		db.setString(1, produto.getName());
		
		db.setDate(2, produto.getFabrica() == null ? new Date() : produto.getFabrica());
		
		if (produto.getValidade() == null)
			db.setNullDate(3);
		else db.setDate(3, produto.getValidade());
		
		db.setInt(4, produto.getCompany().getId());
		db.setInt(5, produto.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Produto produto) throws ModelException {
		
		DBHandler db = new DBHandler();
			
		String sqlDelete = " DELETE FROM produtos "
			         + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, produto.getId());
			
		return db.executeUpdate() > 0;
		}
	}
