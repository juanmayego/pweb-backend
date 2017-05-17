package py.pol.una.ii.pw.mybatis.mappers;

import java.util.List;

import py.pol.una.ii.pw.model.Clientes;

public interface ClientesMapper {
	public void insertCliente(Clientes cliente);

	public Clientes getClienteById(Long idCliente);

	public List<Clientes> getAllCliente();
	
	public List<Clientes> filterCliente(String filter);

	public void updateCliente(Clientes cliente);

	public void deleteCliente(Long idCliente);
	
	public Integer checkFkCliente(Clientes cliente);
}
