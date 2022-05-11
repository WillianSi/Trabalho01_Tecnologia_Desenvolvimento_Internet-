package model.dao;

import java.util.List;

import model.ModelException;
import model.Produto;

public interface ProdutoDAO {
	boolean save(Produto produto) throws ModelException;
	boolean update(Produto produto) throws ModelException;
	boolean delete(Produto produto) throws ModelException;
	List<Produto> listAll() throws ModelException;
	Produto findById(int id) throws ModelException;
}
