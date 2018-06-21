package g.rezza.moch.clientdashboard.holder;

/**
 * Created by Rezza on 9/22/17.
 */

public class TransDtlHolder {
    private String order_no         = "0";
    private String created_date     = "";
    private String sales_channel    = "";
    private String qty              = "";
    private String order_value      = "";
    private String payment_method   = "";


    /*
     * CONSTRUCTOR
     */

    public TransDtlHolder (String pOrder_no,String pCreated_date, String porder_value, String pSales, String qty, String payment_method){
        this.order_no       = pOrder_no;
        this.created_date   = pCreated_date;
        this.order_value    = porder_value;
        this.sales_channel  = pSales;
        this.qty            = qty;
        this.payment_method = payment_method;

    }
    public TransDtlHolder (){

    }

    /*
     * GETTER
     */

    public String getCreatedDate() {
        return created_date;
    }
    public String getOrderno() {
        return order_no;
    }
    public String getQty() {
        return qty;
    }
    public String getSalesChannel() {
        return sales_channel;
    }
    public String getOrderValue() {
        return order_value;
    }
    public String getPaymentMethod() {
        return payment_method;
    }

    /*
     * SETTER
     */

    public void setCreatedDate(String created_date) {
        this.created_date = created_date;
    }
    public void setOrderNo(String trans_id) {
        this.order_no = trans_id;
    }
    public void setChannel(String qty) {
        this.qty = qty;
    }
    public void setSales(String sales) {
        this.sales_channel = sales;
    }
    public void setOrderValue(String order_value) {
        this.order_value = order_value;
    }
    public void setPaymentMethod(String payment_method) {
        this.payment_method = payment_method;
    }
}
