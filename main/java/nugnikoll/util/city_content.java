package nugnikoll.util;

public class city_content{
	public String province;
	public String city;
	public String number;
	public String first_py;
	public String all_py;
	public String all_first_py;

	public void city_content(
		String province, String city, String number, String first_py,
		String all_py, String all_first_py
	){
		this.province = province;
		this.city = city;
		this.number = number;
		this.first_py = first_py;
		this.all_py = all_py;
		this.all_first_py = all_first_py;
	}
	
	@Override
	public String toString(){
		return "province:" + province
			+ ", city:" + city
			+ ", number:" + number
			+ ", first_py:" + first_py
			+ ", all_py:" + all_py
			+ ", all_first_py:" + all_first_py;
	}
}
