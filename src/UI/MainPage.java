package UI;


import Db.DbFunction;
import Db.Enum.ECategoryType;
import Db.Enum.EPaymentMethods;
import Db.Exception.DbConnectException;
import Db.Tables.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainPage extends javax.swing.JFrame {

    private Persons loggedInUser;
    private DbFunction dbFunction;
    private Expenses expenses;
    DefaultTableModel model;

    /**
     * Creates new form MainPage
     */
    public MainPage() {
        initComponents();
        loggedInUser = new Persons();
        dbFunction = new DbFunction();
        expenses = new Expenses();
        this.model = new DefaultTableModel();
        txtf_expenses_date.setText(new Date(System.currentTimeMillis()).toString());
        set_payment_methods_combobox();
        set_categories_combobox();

    }

    public MainPage(Persons loggedInUser) {
        this.loggedInUser = loggedInUser;
        dbFunction = new DbFunction();
        initComponents();

        expenses = new Expenses();
        this.model = new DefaultTableModel();
        txtf_expenses_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));

        lbl_name.setText(loggedInUser.getName());
        lbl_name.setSize(77, 17);
        lbl_surname.setText("");
        txtf_expenses_id.setEditable(false);
        txtf_expenses_id.setBackground(Color.lightGray);
        txtf_expenses_detail_amount.setEditable(false);
        txtf_expenses_detail_cost.setEditable(false);
        txtf_expenses_detail_id.setEditable(false);
        txtf_expenses_detail_expenses_id.setEditable(false);
        rbtn_expenses_detail_date.setVisible(false);
        btn_expenses_detail_delete.setVisible(false);
        set_payment_methods_combobox();
        set_categories_combobox();

    }
    private void updateTable (List<Expenses> expensesList) {
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Id", "Cost", "Amount", "Description", "Category", "Payment Method", "Date"});
        for (Expenses expenses : expensesList) {
            model.addRow(new Object[]{expenses.getId(), expenses.getCost(), expenses.getAmount(), expenses.getDescription(), expenses.getCategory_id(), expenses.getPayment_method_id(), expenses.getDate()});
        }
        tbl_expenses.setModel(model);
    }
    private void updateTableExpensesDetail(List<ExpensesDetails> expensesDetailsList) {
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Id", "Expense Id", "Item", "Cost", "Amount"});
        for (ExpensesDetails expensesDetails : expensesDetailsList) {
            model.addRow(new Object[]{expensesDetails.getId(), expensesDetails.getExpense_id(), expensesDetails.getItem(), expensesDetails.getCost(), expensesDetails.getAmount()});
        }
        tbl_expenses_detail.setModel(model);
    }


    private Persons getLoggedInUser() {
        return loggedInUser;
    }

    private void set_payment_methods_combobox() {
        cmbx_expenses_payment_methods.removeAllItems();
        cmbx_payment_methods_payment_methods.removeAllItems();
        for (EPaymentMethods paymentMethod : EPaymentMethods.values()) {
            cmbx_expenses_payment_methods.addItem(paymentMethod.getDisplayName());
            cmbx_payment_methods_payment_methods.addItem(paymentMethod.getDisplayName());
        }
    }

    private void set_categories_combobox() {
        cmbx_expenses_category.removeAllItems();
        cmbx_categories_categories.removeAllItems();
        try {
            dbFunction.getCategories().forEach(category -> {
                cmbx_expenses_category.addItem(category.getName());
                cmbx_categories_categories.addItem(category.getName());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int getSelectedCategoryId() {
        String selectedCategory = (String) cmbx_expenses_category.getSelectedItem();
        if (selectedCategory != null) {
            ECategoryType categoryType = ECategoryType.valueOf(selectedCategory);
            return categoryType.getId();
        } else {
            throw new IllegalArgumentException("No category selected");
        }
    }

    private void getExpensesData() throws DbConnectException, SQLException {
        dbFunction = new DbFunction();
        int userId = loggedInUser.getId();
        List<Expenses> expensesList = dbFunction.getExpensesByPersonId(userId);
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Id", "Cost", "Amount", "Description", "Category", "Payment Method", "Date"});
        for (Expenses expenses : expensesList) {
            model.addRow(new Object[]{expenses.getId(), expenses.getCost(), expenses.getAmount(), expenses.getDescription(), expenses.getCategory_id(), expenses.getPayment_method_id(), expenses.getDate()});
        }
        tbl_expenses.setModel(model);
    }
    private void getExpensesDetailsData(){
        try {
            List<ExpensesDetails> expensesDetailsList = dbFunction.getExpensesDetailByPersonId(loggedInUser.getId());
            model = new DefaultTableModel();
            model.setColumnIdentifiers(new Object[]{"Id", "Expense Id", "Item", "Cost", "Amount"});
            for (ExpensesDetails expensesDetails : expensesDetailsList) {
                model.addRow(new Object[]{expensesDetails.getId(), expensesDetails.getExpense_id(), expensesDetails.getItem(), expensesDetails.getCost(), expensesDetails.getAmount()});
            }
            tbl_expenses_detail.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCategoriesData() {
        try {
            List<Categories> categoriesList = dbFunction.getCategories();
            model = new DefaultTableModel();
            model.setColumnIdentifiers(new Object[]{"Id", "Name"});
            for (Categories categories : categoriesList) {
                model.addRow(new Object[]{categories.getId(), categories.getName()});
            }
            tbl_expenses_categories.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPaymentMethodsData() {
        try {
            List<PaymentMethods> paymentMethodsList = dbFunction.getPaymentMethods();
            model = new DefaultTableModel();
            model.setColumnIdentifiers(new Object[]{"Id", "Name"});

            for (PaymentMethods paymentMethods : paymentMethodsList) {
                String paymentMethodDisplayName = String.valueOf(paymentMethods.getPaymentMethod());

                // Enum sabitlerini display name'leri ile eşleştirerek bulma
                try {
                    EPaymentMethods methodEnum = EPaymentMethods.fromDisplayName(paymentMethodDisplayName);
                    model.addRow(new Object[]{paymentMethods.getId(), methodEnum.getDisplayName()});
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown payment method: " + paymentMethodDisplayName);
                    model.addRow(new Object[]{paymentMethods.getId(), paymentMethodDisplayName});
                }
            }

            tbl_payment_methods.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        btngrp_expenses = new javax.swing.ButtonGroup();
        pnl_all = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        tbdp_db = new javax.swing.JTabbedPane();
        pnl_expenses = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_expenses = new javax.swing.JTable();
        lbl_expenses_id = new javax.swing.JLabel();
        lbl_expenses_cost = new javax.swing.JLabel();
        lbl_expenses_amount = new javax.swing.JLabel();
        lbl_expenses_description = new javax.swing.JLabel();
        lbl_expenses_category = new javax.swing.JLabel();
        lbl_expenses_payment_method = new javax.swing.JLabel();
        lbl_expenses_date = new javax.swing.JLabel();
        txtf_expenses_id = new javax.swing.JTextField();
        txtf_expenses_cost = new javax.swing.JTextField();
        txtf_expenses_amount = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        txta_expenses_description = new javax.swing.JTextArea();
        txtf_expenses_date = new javax.swing.JTextField();
        cmbx_expenses_category = new javax.swing.JComboBox<>();
        cmbx_expenses_payment_methods = new javax.swing.JComboBox<>();
        btn_expenses_add = new javax.swing.JButton();
        btn_expenses_update = new javax.swing.JButton();
        lbl_expenses_search = new javax.swing.JLabel();
        txtf_expenses_search = new javax.swing.JTextField();
        rbtn_expenses_asc = new javax.swing.JRadioButton();
        rbtn_expenses_desc = new javax.swing.JRadioButton();
        rbtn_expenses_date = new javax.swing.JRadioButton();
        btn_expenses_search = new javax.swing.JButton();
        btn_expenses_delete = new javax.swing.JButton();
        btn_expenses_clear = new javax.swing.JButton();
        pnl_expenses_detail = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_expenses_detail = new javax.swing.JTable();
        lbl_expenses_detail_search = new javax.swing.JLabel();
        txtf_expenses_detail_search = new javax.swing.JTextField();
        btn_expenses_detail_search = new javax.swing.JButton();
        rbtn_expenses_detail_asc = new javax.swing.JRadioButton();
        rbtn_expenses_detail_desc = new javax.swing.JRadioButton();
        rbtn_expenses_detail_date = new javax.swing.JRadioButton();
        btn_expenses_detail_update = new javax.swing.JButton();
        btn_expenses_detail_delete = new javax.swing.JButton();
        btn_expenses_detail_clear = new javax.swing.JButton();
        lbl_expenses_detail_id = new javax.swing.JLabel();
        txtf_expenses_detail_id = new javax.swing.JTextField();
        lbl_expenses_detail_expenses_id = new javax.swing.JLabel();
        txtf_expenses_detail_expenses_id = new javax.swing.JTextField();
        lbl_expenses_detail_item = new javax.swing.JLabel();
        lbl_expenses_detail_amount = new javax.swing.JLabel();
        txtf_expenses_detail_amount = new javax.swing.JTextField();
        txtf_expenses_detail_cost = new javax.swing.JTextField();
        lbl_expenses_detail_cost = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txta_expenses_detail_item = new javax.swing.JTextArea();
        pnl_categories = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_expenses_categories = new javax.swing.JTable();
        lbl_categories_name = new javax.swing.JLabel();
        lbl_categories_categories = new javax.swing.JLabel();
        txtf_categories_id = new javax.swing.JTextField();
        cmbx_categories_categories = new javax.swing.JComboBox<>();
        btn_categories_clear = new javax.swing.JButton();
        pnl_payment_methods = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_payment_methods = new javax.swing.JTable();
        btn_payment_methods_clear = new javax.swing.JButton();
        cmbx_payment_methods_payment_methods = new javax.swing.JComboBox<>();
        lbl_payment_methods_categories = new javax.swing.JLabel();
        lbl_payment_methods_id = new javax.swing.JLabel();
        txtf_payment_methods_id = new javax.swing.JTextField();
        lbl_name = new javax.swing.JLabel();
        lbl_surname = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menu_file = new javax.swing.JMenu();
        menu_item_export_excel = new javax.swing.JMenuItem();
        menu_item_import_excel = new javax.swing.JMenuItem();
        menu_item_export_xml = new javax.swing.JMenuItem();
        menu_item_import_xml = new javax.swing.JMenuItem();
        menu_edit = new javax.swing.JMenu();
        menu_item_profile = new javax.swing.JMenuItem();
        menu_item_statistic = new javax.swing.JMenuItem();
        menu_categories = new javax.swing.JMenu();
        menu_expenses_detail = new javax.swing.JMenu();
        menu_payment_methods = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tbdp_db.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tbdp_dbStateChanged(evt);
            }
        });
        tbdp_db.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbdp_dbMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tbdp_dbMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tbdp_dbMouseExited(evt);
            }
        });

        pnl_expenses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_expensesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnl_expensesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnl_expensesMouseExited(evt);
            }
        });

        tbl_expenses.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {},
                        {},
                        {},
                        {}
                },
                new String [] {

                }
        ));
        tbl_expenses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_expensesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_expenses);

        lbl_expenses_id.setText("Id");

        lbl_expenses_cost.setText("Cost");

        lbl_expenses_amount.setText("Amount");

        lbl_expenses_description.setText("Description");

        lbl_expenses_category.setText("Category");

        lbl_expenses_payment_method.setText("Payment Method");

        lbl_expenses_date.setText("Date");

        txta_expenses_description.setColumns(20);
        txta_expenses_description.setRows(5);
        jScrollPane5.setViewportView(txta_expenses_description);

        cmbx_expenses_category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbx_expenses_payment_methods.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btn_expenses_add.setBackground(new java.awt.Color(98, 194, 242));
        btn_expenses_add.setText("Add");
        btn_expenses_add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_expenses_addMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_expenses_addMouseExited(evt);
            }
        });
        btn_expenses_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_expenses_addActionPerformed(evt);
            }
        });

        btn_expenses_update.setBackground(new java.awt.Color(226, 165, 165));
        btn_expenses_update.setText("Update");
        btn_expenses_update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_expenses_updateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_expenses_updateMouseExited(evt);
            }
        });
        btn_expenses_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_expenses_updateActionPerformed(evt);
            }
        });

        lbl_expenses_search.setText("Search");

        btngrp_expenses.add(rbtn_expenses_asc);
        rbtn_expenses_asc.setText("Asc");
        rbtn_expenses_asc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_expenses_ascActionPerformed(evt);
            }
        });

        btngrp_expenses.add(rbtn_expenses_desc);
        rbtn_expenses_desc.setText("Desc");
        rbtn_expenses_desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_expenses_descActionPerformed(evt);
            }
        });

        btngrp_expenses.add(rbtn_expenses_date);
        rbtn_expenses_date.setText("Date");
        rbtn_expenses_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_expenses_dateActionPerformed(evt);
            }
        });

        btn_expenses_search.setText("Search");
        btn_expenses_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_expenses_searchActionPerformed(evt);
            }
        });

        btn_expenses_delete.setBackground(new java.awt.Color(246, 26, 70));
        btn_expenses_delete.setText("Delete");
        btn_expenses_delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_expenses_deleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_expenses_deleteMouseExited(evt);
            }
        });
        btn_expenses_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_expenses_deleteActionPerformed(evt);
            }
        });

        btn_expenses_clear.setBackground(new java.awt.Color(204, 204, 204));
        btn_expenses_clear.setText("Clear");
        btn_expenses_clear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_expenses_clearMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_expenses_clearMouseExited(evt);
            }
        });
        btn_expenses_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_expenses_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_expensesLayout = new javax.swing.GroupLayout(pnl_expenses);
        pnl_expenses.setLayout(pnl_expensesLayout);
        pnl_expensesLayout.setHorizontalGroup(
                pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18))
                                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lbl_expenses_search, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtf_expenses_search, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btn_expenses_search)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(rbtn_expenses_asc)
                                                .addGap(18, 18, 18)
                                                .addComponent(rbtn_expenses_desc)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rbtn_expenses_date)
                                                .addGap(40, 40, 40)))
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lbl_expenses_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbl_expenses_cost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbl_expenses_amount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbl_expenses_description, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbl_expenses_category, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbl_expenses_payment_method, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbl_expenses_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtf_expenses_id, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtf_expenses_cost, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtf_expenses_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 25, Short.MAX_VALUE))
                                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                        .addComponent(txtf_expenses_date, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                                                        .addComponent(cmbx_expenses_payment_methods, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(cmbx_expenses_category, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                .addContainerGap())))
                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btn_expenses_add, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_expenses_update, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_expenses_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_expenses_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_expensesLayout.setVerticalGroup(
                pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_expenses_search)
                                        .addComponent(txtf_expenses_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(rbtn_expenses_asc)
                                        .addComponent(rbtn_expenses_desc)
                                        .addComponent(rbtn_expenses_date)
                                        .addComponent(btn_expenses_search))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btn_expenses_add, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_expenses_update, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_expenses_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_expenses_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26))
                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_expenses_id)
                                        .addComponent(txtf_expenses_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_expenses_cost)
                                        .addComponent(txtf_expenses_cost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_expenses_amount)
                                        .addComponent(txtf_expenses_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                                .addGap(62, 62, 62)
                                                .addComponent(lbl_expenses_description))
                                        .addGroup(pnl_expensesLayout.createSequentialGroup()
                                                .addGap(28, 28, 28)
                                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(36, 36, 36)
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_expenses_category)
                                        .addComponent(cmbx_expenses_category, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_expenses_payment_method)
                                        .addComponent(cmbx_expenses_payment_methods, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(pnl_expensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_expenses_date)
                                        .addComponent(txtf_expenses_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbdp_db.addTab("Expenses", pnl_expenses);

        pnl_expenses_detail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_expenses_detailMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnl_expenses_detailMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnl_expenses_detailMouseExited(evt);
            }
        });

        tbl_expenses_detail.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {},
                        {},
                        {},
                        {}
                },
                new String [] {

                }
        ));
        tbl_expenses_detail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_expenses_detailMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_expenses_detail);

        lbl_expenses_detail_search.setText("Search");

        btn_expenses_detail_search.setText("Search");
        btn_expenses_detail_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_expenses_detail_searchActionPerformed(evt);
            }
        });

        btngrp_expenses.add(rbtn_expenses_detail_asc);
        rbtn_expenses_detail_asc.setText("Asc");
        rbtn_expenses_detail_asc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_expenses_detail_ascActionPerformed(evt);
            }
        });

        btngrp_expenses.add(rbtn_expenses_detail_desc);
        rbtn_expenses_detail_desc.setText("Desc");
        rbtn_expenses_detail_desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_expenses_detail_descActionPerformed(evt);
            }
        });

        btngrp_expenses.add(rbtn_expenses_detail_date);
        rbtn_expenses_detail_date.setText("Date");
        rbtn_expenses_detail_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_expenses_detail_dateActionPerformed(evt);
            }
        });

        btn_expenses_detail_update.setBackground(new java.awt.Color(226, 165, 165));
        btn_expenses_detail_update.setText("Update");
        btn_expenses_detail_update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_expenses_detail_updateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_expenses_detail_updateMouseExited(evt);
            }
        });
        btn_expenses_detail_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_expenses_detail_updateActionPerformed(evt);
            }
        });

        btn_expenses_detail_delete.setBackground(new java.awt.Color(246, 26, 70));
        btn_expenses_detail_delete.setText("Delete");
        btn_expenses_detail_delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_expenses_detail_deleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_expenses_detail_deleteMouseExited(evt);
            }
        });
        btn_expenses_detail_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_expenses_detail_deleteActionPerformed(evt);
            }
        });

        btn_expenses_detail_clear.setBackground(new java.awt.Color(204, 204, 204));
        btn_expenses_detail_clear.setText("Clear");
        btn_expenses_detail_clear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_expenses_detail_clearMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_expenses_detail_clearMouseExited(evt);
            }
        });
        btn_expenses_detail_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_expenses_detail_clearActionPerformed(evt);
            }
        });

        lbl_expenses_detail_id.setText("Id");

        lbl_expenses_detail_expenses_id.setText("E-Id");

        lbl_expenses_detail_item.setText("Item");

        lbl_expenses_detail_amount.setText("Amount");

        lbl_expenses_detail_cost.setText("Cost");

        txta_expenses_detail_item.setColumns(20);
        txta_expenses_detail_item.setRows(5);
        jScrollPane6.setViewportView(txta_expenses_detail_item);

        javax.swing.GroupLayout pnl_expenses_detailLayout = new javax.swing.GroupLayout(pnl_expenses_detail);
        pnl_expenses_detail.setLayout(pnl_expenses_detailLayout);
        pnl_expenses_detailLayout.setHorizontalGroup(
                pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_expenses_detailLayout.createSequentialGroup()
                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_expenses_detailLayout.createSequentialGroup()
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(26, 26, 26)
                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(pnl_expenses_detailLayout.createSequentialGroup()
                                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(lbl_expenses_detail_cost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(lbl_expenses_detail_amount))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtf_expenses_detail_cost, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                                                        .addComponent(txtf_expenses_detail_amount)))
                                                        .addGroup(pnl_expenses_detailLayout.createSequentialGroup()
                                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(lbl_expenses_detail_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(lbl_expenses_detail_expenses_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(lbl_expenses_detail_item))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                                        .addComponent(txtf_expenses_detail_expenses_id, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                                                        .addComponent(txtf_expenses_detail_id)))))
                                        .addGroup(pnl_expenses_detailLayout.createSequentialGroup()
                                                .addComponent(lbl_expenses_detail_search, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtf_expenses_detail_search, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btn_expenses_detail_search)
                                                .addGap(26, 26, 26)
                                                .addComponent(rbtn_expenses_detail_asc)
                                                .addGap(18, 18, 18)
                                                .addComponent(rbtn_expenses_detail_desc)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rbtn_expenses_detail_date))
                                        .addGroup(pnl_expenses_detailLayout.createSequentialGroup()
                                                .addGap(72, 72, 72)
                                                .addComponent(btn_expenses_detail_update, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btn_expenses_detail_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btn_expenses_detail_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(60, Short.MAX_VALUE))
        );
        pnl_expenses_detailLayout.setVerticalGroup(
                pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_expenses_detailLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_expenses_detail_search)
                                        .addComponent(txtf_expenses_detail_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(rbtn_expenses_detail_asc)
                                        .addComponent(rbtn_expenses_detail_desc)
                                        .addComponent(rbtn_expenses_detail_date)
                                        .addComponent(btn_expenses_detail_search))
                                .addGap(18, 18, 18)
                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_expenses_detailLayout.createSequentialGroup()
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(43, 43, 43)
                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btn_expenses_detail_update, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btn_expenses_detail_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btn_expenses_detail_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(pnl_expenses_detailLayout.createSequentialGroup()
                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lbl_expenses_detail_id)
                                                        .addComponent(txtf_expenses_detail_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lbl_expenses_detail_expenses_id)
                                                        .addComponent(txtf_expenses_detail_expenses_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(pnl_expenses_detailLayout.createSequentialGroup()
                                                                .addGap(63, 63, 63)
                                                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_expenses_detailLayout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(lbl_expenses_detail_item)
                                                                .addGap(119, 119, 119)))
                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lbl_expenses_detail_cost)
                                                        .addComponent(txtf_expenses_detail_cost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(pnl_expenses_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lbl_expenses_detail_amount)
                                                        .addComponent(txtf_expenses_detail_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(144, 144, 144))))
        );

        tbdp_db.addTab("Expenses Detail", pnl_expenses_detail);

        pnl_categories.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_categoriesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnl_categoriesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnl_categoriesMouseExited(evt);
            }
        });

        tbl_expenses_categories.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {},
                        {},
                        {},
                        {}
                },
                new String [] {

                }
        ));
        tbl_expenses_categories.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_expenses_categoriesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbl_expenses_categories);

        lbl_categories_name.setText("Id");

        lbl_categories_categories.setText("Categories");

        cmbx_categories_categories.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btn_categories_clear.setBackground(new java.awt.Color(204, 204, 204));
        btn_categories_clear.setText("Clear");
        btn_categories_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_categories_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_categoriesLayout = new javax.swing.GroupLayout(pnl_categories);
        pnl_categories.setLayout(pnl_categoriesLayout);
        pnl_categoriesLayout.setHorizontalGroup(
                pnl_categoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_categoriesLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnl_categoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_categoriesLayout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(pnl_categoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(pnl_categoriesLayout.createSequentialGroup()
                                                                .addComponent(lbl_categories_name, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtf_categories_id, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(pnl_categoriesLayout.createSequentialGroup()
                                                                .addComponent(lbl_categories_categories, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cmbx_categories_categories, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                        .addGroup(pnl_categoriesLayout.createSequentialGroup()
                                                .addGap(74, 74, 74)
                                                .addComponent(btn_categories_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 71, Short.MAX_VALUE))
        );
        pnl_categoriesLayout.setVerticalGroup(
                pnl_categoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_categoriesLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(pnl_categoriesLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(pnl_categoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_categories_name)
                                        .addComponent(txtf_categories_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pnl_categoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_categories_categories)
                                        .addComponent(cmbx_categories_categories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addComponent(btn_categories_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(410, Short.MAX_VALUE))
        );

        tbdp_db.addTab("Categories", pnl_categories);

        pnl_payment_methods.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_payment_methodsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnl_payment_methodsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnl_payment_methodsMouseExited(evt);
            }
        });

        tbl_payment_methods.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {},
                        {},
                        {},
                        {}
                },
                new String [] {

                }
        ));
        tbl_payment_methods.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_payment_methodsMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbl_payment_methods);

        btn_payment_methods_clear.setBackground(new java.awt.Color(204, 204, 204));
        btn_payment_methods_clear.setText("Clear");
        btn_payment_methods_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_payment_methods_clearActionPerformed(evt);
            }
        });

        cmbx_payment_methods_payment_methods.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbl_payment_methods_categories.setText("Categories");

        lbl_payment_methods_id.setText("Id");

        javax.swing.GroupLayout pnl_payment_methodsLayout = new javax.swing.GroupLayout(pnl_payment_methods);
        pnl_payment_methods.setLayout(pnl_payment_methodsLayout);
        pnl_payment_methodsLayout.setHorizontalGroup(
                pnl_payment_methodsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_payment_methodsLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addGroup(pnl_payment_methodsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_payment_methodsLayout.createSequentialGroup()
                                                .addComponent(lbl_payment_methods_id, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtf_payment_methods_id, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnl_payment_methodsLayout.createSequentialGroup()
                                                .addComponent(lbl_payment_methods_categories, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmbx_payment_methods_payment_methods, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnl_payment_methodsLayout.createSequentialGroup()
                                                .addGap(56, 56, 56)
                                                .addComponent(btn_payment_methods_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 58, Short.MAX_VALUE))
        );
        pnl_payment_methodsLayout.setVerticalGroup(
                pnl_payment_methodsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_payment_methodsLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 248, Short.MAX_VALUE))
                        .addGroup(pnl_payment_methodsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnl_payment_methodsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_payment_methods_id)
                                        .addComponent(txtf_payment_methods_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pnl_payment_methodsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_payment_methods_categories)
                                        .addComponent(cmbx_payment_methods_payment_methods, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addComponent(btn_payment_methods_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbdp_db.addTab("Payment Methods", pnl_payment_methods);

        lbl_name.setText("Name");

        lbl_surname.setText("Surname");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_name, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_surname, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tbdp_db, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_surname)
                                        .addComponent(lbl_name, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tbdp_db)
                                .addContainerGap())
        );

        javax.swing.GroupLayout pnl_allLayout = new javax.swing.GroupLayout(pnl_all);
        pnl_all.setLayout(pnl_allLayout);
        pnl_allLayout.setHorizontalGroup(
                pnl_allLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnl_allLayout.setVerticalGroup(
                pnl_allLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menu_file.setText("File");
        menu_file.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu_fileMouseEntered(evt);
            }
        });

        menu_item_export_excel.setText("Export Excel");
        menu_item_export_excel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_export_excelActionPerformed(evt);
            }
        });
        menu_file.add(menu_item_export_excel);

        menu_item_import_excel.setText("Import Excel");
        menu_item_import_excel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_import_excelActionPerformed(evt);
            }
        });
        menu_file.add(menu_item_import_excel);

        menu_item_export_xml.setText("Export xml");
        menu_item_export_xml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_export_xmlActionPerformed(evt);
            }
        });
        menu_file.add(menu_item_export_xml);

        menu_item_import_xml.setText("Import xml");
        menu_item_import_xml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_import_xmlActionPerformed(evt);
            }
        });
        menu_file.add(menu_item_import_xml);

        jMenuBar1.add(menu_file);

        menu_edit.setText("Edit");
        menu_edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_editMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu_editMouseEntered(evt);
            }
        });

        menu_item_profile.setText("Profile");
        menu_item_profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_profileActionPerformed(evt);
            }
        });
        menu_edit.add(menu_item_profile);

        menu_item_statistic.setText("Statistic");
        menu_edit.add(menu_item_statistic);

        jMenuBar1.add(menu_edit);

        menu_categories.setText("Categories");
        menu_categories.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_categoriesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu_categoriesMouseEntered(evt);
            }
        });
        jMenuBar1.add(menu_categories);

        menu_expenses_detail.setText("Expenses Detail");
        menu_expenses_detail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_expenses_detailMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu_expenses_detailMouseEntered(evt);
            }
        });
        jMenuBar1.add(menu_expenses_detail);

        menu_payment_methods.setText("Payment Methods");
        menu_payment_methods.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_payment_methodsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu_payment_methodsMouseEntered(evt);
            }
        });
        jMenuBar1.add(menu_payment_methods);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnl_all, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pnl_all, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void menu_item_export_excelActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_item_import_excelActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_item_export_xmlActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_item_import_xmlActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void tbdp_dbStateChanged(javax.swing.event.ChangeEvent evt) {
        // TODO add your handling code here:
        JTabbedPane sourceTabbedPane = (JTabbedPane) evt.getSource();
        int selectedIndex = sourceTabbedPane.getSelectedIndex();
        String tabTitle = sourceTabbedPane.getTitleAt(selectedIndex);
        if (tabTitle.equals("Expenses")) {
            try {
                getExpensesData();
            } catch (DbConnectException | SQLException e) {
                e.printStackTrace();
            }
        }else if(tabTitle.equals("Categories")){
            getCategoriesData();
        }else if(tabTitle.equals("Payment Methods")){
            getPaymentMethodsData();
        } else if (tabTitle.equals("Expenses Detail")) {
            try {
                getExpensesDetailsData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void tbdp_dbMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_expensesMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:

    }

    private void tbdp_dbMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void tbdp_dbMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_expensesMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_expensesMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_expenses_detailMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_expenses_detailMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_expenses_detailMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_categoriesMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_categoriesMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_categoriesMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_payment_methodsMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_payment_methodsMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void pnl_payment_methodsMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void tbl_expensesMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        int selectedIndex = tbl_expenses.getSelectedRow();
        if (selectedIndex != -1) {
            txtf_expenses_id.setText(tbl_expenses.getValueAt(selectedIndex, 0).toString());
            txtf_expenses_cost.setText(tbl_expenses.getValueAt(selectedIndex, 1).toString());
            txtf_expenses_amount.setText(tbl_expenses.getValueAt(selectedIndex, 2).toString());
            txta_expenses_description.setText(tbl_expenses.getValueAt(selectedIndex, 3).toString());
            cmbx_expenses_category.setSelectedIndex((int) tbl_expenses.getValueAt(selectedIndex, 4) - 1);
            cmbx_expenses_payment_methods.setSelectedIndex((int) tbl_expenses.getValueAt(selectedIndex, 5) - 1);
            txtf_expenses_date.setText(tbl_expenses.getValueAt(selectedIndex, 6).toString());
        }
    }

    private void tbl_expenses_detailMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        int selectedIndex = tbl_expenses_detail.getSelectedRow();
        if (selectedIndex != -1) {
            txtf_expenses_detail_id.setText(tbl_expenses_detail.getValueAt(selectedIndex, 0).toString());
            txtf_expenses_detail_expenses_id.setText(tbl_expenses_detail.getValueAt(selectedIndex, 1).toString());
            txta_expenses_detail_item.setText(tbl_expenses_detail.getValueAt(selectedIndex, 2).toString());
            txtf_expenses_detail_cost.setText(tbl_expenses_detail.getValueAt(selectedIndex, 3).toString());
            txtf_expenses_detail_amount.setText(tbl_expenses_detail.getValueAt(selectedIndex, 4).toString());
        }

    }

    private void tbl_expenses_categoriesMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        int selectedIndex = tbl_expenses_categories.getSelectedRow();
        if (selectedIndex != -1) {
            txtf_categories_id.setText(tbl_expenses_categories.getValueAt(selectedIndex, 0).toString());
            cmbx_categories_categories.setSelectedItem(tbl_expenses_categories.getValueAt(selectedIndex, 1).toString());
        }
    }

    private void tbl_payment_methodsMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        int selectedIndex = tbl_payment_methods.getSelectedRow();
        if (selectedIndex != -1) {
            txtf_payment_methods_id.setText(tbl_payment_methods.getValueAt(selectedIndex, 0).toString());
            cmbx_payment_methods_payment_methods.setSelectedIndex((int) tbl_payment_methods.getValueAt(selectedIndex, 1) - 1);
        }

    }

    private void menu_payment_methodsMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_expenses_detailMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_categoriesMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_editMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_fileMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_editMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_categoriesMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_expenses_detailMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_payment_methodsMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void menu_item_profileActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btn_expenses_addActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // TODO add your handling code here:
            // change the get person id (get a login funct with person id)
            int loggedInUserId = loggedInUser.getId();
            BigDecimal cost = new BigDecimal(txtf_expenses_cost.getText());
            BigDecimal amount = new BigDecimal(txtf_expenses_amount.getText());
            String description = txta_expenses_description.getText();
            int category_id = getSelectedCategoryId();
            int payment_method_id = cmbx_expenses_payment_methods.getSelectedIndex() + 1;
            Date date = Date.valueOf(txtf_expenses_date.getText());


            //create expense object
            Expenses expenses = new Expenses();
            expenses.setPerson_id(loggedInUserId);
            expenses.setCost(cost);
            expenses.setAmount(amount);
            expenses.setDescription(description);
            expenses.setCategory_id(category_id);
            expenses.setPayment_method_id(payment_method_id);
            expenses.setDate(date);

            //add sql
            dbFunction.insertExpense(expenses);
            JOptionPane.showMessageDialog(this, "Expense added successfully!");
            getExpensesData();

        } catch (DbConnectException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add expense: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btn_expenses_updateActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            // change the get person id (get a login funct with person id)
            int id = Integer.parseInt(txtf_expenses_id.getText());
            BigDecimal cost = new BigDecimal(txtf_expenses_cost.getText());
            BigDecimal amount = new BigDecimal(txtf_expenses_amount.getText());
            String description = txta_expenses_description.getText();
            int category_id = getSelectedCategoryId();
            int payment_method_id = cmbx_expenses_payment_methods.getSelectedIndex() + 1;
            Date date = Date.valueOf(txtf_expenses_date.getText());

            //create expense object
            Expenses expenses = new Expenses();
            expenses.setId(id);
            expenses.setPerson_id(loggedInUser.getId());
            expenses.setCost(cost);
            expenses.setAmount(amount);
            expenses.setDescription(description);
            expenses.setCategory_id(category_id);
            expenses.setPayment_method_id(payment_method_id);
            expenses.setDate(date);

            //add sql
            dbFunction.updateExpense(expenses);
            JOptionPane.showMessageDialog(this, "Expense updated successfully!");

        } catch (DbConnectException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update expense: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btn_expenses_deleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            int expenseId = Integer.parseInt(txtf_expenses_id.getText());
            Expenses expenses = new Expenses();
            expenses.setId(expenseId);

            dbFunction.deleteExpense(expenses);
            JOptionPane.showMessageDialog(this, "Expense deleted successfully!");
        } catch (DbConnectException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete expense: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btn_expenses_addMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_add.setBackground(new Color(12, 93, 166));
    }

    private void btn_expenses_addMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_add.setBackground(new Color(98,194,242));

    }

    private void btn_expenses_updateMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_update.setBackground(new Color(228, 120, 241));
    }

    private void btn_expenses_updateMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
     btn_expenses_update.setBackground(new Color(226,165,165));
    }

    private void btn_expenses_deleteMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_delete.setBackground(new Color(92, 0, 0));
    }

    private void btn_expenses_deleteMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_delete.setBackground(new Color(246, 26, 70));
    }
    private void btn_expenses_clearActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        btn_expenses_clear.setBackground(new Color(99, 99, 99));
        tbl_expenses.clearSelection();
        txtf_expenses_id.setText("");
        txtf_expenses_cost.setText("");
        txtf_expenses_amount.setText("");
        txta_expenses_description.setText("");
        cmbx_expenses_category.setSelectedIndex(0);
        cmbx_expenses_payment_methods.setSelectedIndex(0);
        btngrp_expenses.clearSelection();
        try {
            getExpensesData();
        } catch (DbConnectException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtf_expenses_date.setText(new Date(System.currentTimeMillis()).toString());
    }

    private void btn_expenses_clearMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_clear.setBackground(new Color(165, 163, 163));

    }

    private void btn_expenses_clearMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_clear.setBackground(new Color(204, 204, 204));
    }



    private void btn_expenses_searchActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String search = txtf_expenses_search.getText();
        try {
            List<Expenses> expenses = dbFunction.searchExpenses(search);
            updateTable(expenses);
        } catch (DbConnectException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void rbtn_expenses_dateActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        List<Expenses> expenses = dbFunction.getExpensesSorted("date");
        updateTable(expenses);
    }

    private void rbtn_expenses_descActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        List<Expenses> expenses = dbFunction.getExpensesSorted("desc");
        updateTable(expenses);
    }

    private void rbtn_expenses_ascActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        List<Expenses> expenses = dbFunction.getExpensesSorted("asc");
        updateTable(expenses);
    }


    private void btn_payment_methods_clearActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        txtf_payment_methods_id.setText("");
        //combobox degistirmeye usendim
        cmbx_payment_methods_payment_methods.setSelectedIndex(0);


    }

    private void btn_categories_clearActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        txtf_categories_id.setText("");
        cmbx_categories_categories.setSelectedIndex(0);
    }


    private void btn_expenses_detail_searchActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String search = txtf_expenses_detail_search.getText();
        try {
            List<ExpensesDetails> expensesDetails = dbFunction.searchExpenseDetails(search);
            updateTableExpensesDetail(expensesDetails);
        } catch (DbConnectException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void rbtn_expenses_detail_ascActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        List<ExpensesDetails> expensesDetails = dbFunction.getExpensesDetailsSorted("asc");
        updateTableExpensesDetail(expensesDetails);
    }

    private void rbtn_expenses_detail_descActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        List<ExpensesDetails> expensesDetails = dbFunction.getExpensesDetailsSorted("desc");
        updateTableExpensesDetail(expensesDetails);
    }

    private void rbtn_expenses_detail_dateActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        List<ExpensesDetails> expensesDetails = dbFunction.getExpensesDetailsSorted("date");
        updateTableExpensesDetail(expensesDetails);
    }

    private void btn_expenses_detail_updateMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_detail_update.setBackground(new Color(12, 93, 166));
    }

    private void btn_expenses_detail_updateMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_detail_update.setBackground(new Color(98,194,242));
    }

    private void btn_expenses_detail_updateActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String item = txta_expenses_detail_item.getText();

        ExpensesDetails expensesDetails = new ExpensesDetails();
        expensesDetails.setItem(item);
        expensesDetails.setId(Integer.parseInt(txtf_expenses_detail_id.getText()));

        try {
            dbFunction.updateExpenseDetails(expensesDetails);
            JOptionPane.showMessageDialog(this, "Expense detail updated successfully!");
        } catch (DbConnectException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getExpensesDetailsData();
    }

    private void btn_expenses_detail_deleteMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        int id = Integer.parseInt(txtf_expenses_detail_id.getText());

    }

    private void btn_expenses_detail_deleteMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void btn_expenses_detail_deleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btn_expenses_detail_clearMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_detail_clear.setBackground(new Color(165, 163, 163));
    }

    private void btn_expenses_detail_clearMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        btn_expenses_detail_clear.setBackground(new Color(204, 204, 204));

    }

    private void btn_expenses_detail_clearActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        tbl_expenses_detail.clearSelection();
        txtf_expenses_detail_expenses_id.setText("");
        txtf_expenses_detail_id.setText("");
        txta_expenses_detail_item.setText("");
        txtf_expenses_detail_cost.setText("");
        txtf_expenses_detail_amount.setText("");


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btn_categories_clear;
    private javax.swing.JButton btn_expenses_add;
    private javax.swing.JButton btn_expenses_clear;
    private javax.swing.JButton btn_expenses_delete;
    private javax.swing.JButton btn_expenses_detail_clear;
    private javax.swing.JButton btn_expenses_detail_delete;
    private javax.swing.JButton btn_expenses_detail_search;
    private javax.swing.JButton btn_expenses_detail_update;
    private javax.swing.JButton btn_expenses_search;
    private javax.swing.JButton btn_expenses_update;
    private javax.swing.JButton btn_payment_methods_clear;
    private javax.swing.ButtonGroup btngrp_expenses;
    private javax.swing.JComboBox<String> cmbx_categories_categories;
    private javax.swing.JComboBox<String> cmbx_expenses_category;
    private javax.swing.JComboBox<String> cmbx_expenses_payment_methods;
    private javax.swing.JComboBox<String> cmbx_payment_methods_payment_methods;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lbl_categories_categories;
    private javax.swing.JLabel lbl_categories_name;
    private javax.swing.JLabel lbl_expenses_amount;
    private javax.swing.JLabel lbl_expenses_category;
    private javax.swing.JLabel lbl_expenses_cost;
    private javax.swing.JLabel lbl_expenses_date;
    private javax.swing.JLabel lbl_expenses_description;
    private javax.swing.JLabel lbl_expenses_detail_amount;
    private javax.swing.JLabel lbl_expenses_detail_cost;
    private javax.swing.JLabel lbl_expenses_detail_expenses_id;
    private javax.swing.JLabel lbl_expenses_detail_id;
    private javax.swing.JLabel lbl_expenses_detail_item;
    private javax.swing.JLabel lbl_expenses_detail_search;
    private javax.swing.JLabel lbl_expenses_id;
    private javax.swing.JLabel lbl_expenses_payment_method;
    private javax.swing.JLabel lbl_expenses_search;
    private javax.swing.JLabel lbl_name;
    private javax.swing.JLabel lbl_payment_methods_categories;
    private javax.swing.JLabel lbl_payment_methods_id;
    private javax.swing.JLabel lbl_surname;
    private javax.swing.JMenu menu_categories;
    private javax.swing.JMenu menu_edit;
    private javax.swing.JMenu menu_expenses_detail;
    private javax.swing.JMenu menu_file;
    private javax.swing.JMenuItem menu_item_export_excel;
    private javax.swing.JMenuItem menu_item_export_xml;
    private javax.swing.JMenuItem menu_item_import_excel;
    private javax.swing.JMenuItem menu_item_import_xml;
    private javax.swing.JMenuItem menu_item_profile;
    private javax.swing.JMenuItem menu_item_statistic;
    private javax.swing.JMenu menu_payment_methods;
    private javax.swing.JPanel pnl_all;
    private javax.swing.JPanel pnl_categories;
    private javax.swing.JPanel pnl_expenses;
    private javax.swing.JPanel pnl_expenses_detail;
    private javax.swing.JPanel pnl_payment_methods;
    private javax.swing.JRadioButton rbtn_expenses_asc;
    private javax.swing.JRadioButton rbtn_expenses_date;
    private javax.swing.JRadioButton rbtn_expenses_desc;
    private javax.swing.JRadioButton rbtn_expenses_detail_asc;
    private javax.swing.JRadioButton rbtn_expenses_detail_date;
    private javax.swing.JRadioButton rbtn_expenses_detail_desc;
    private javax.swing.JTabbedPane tbdp_db;
    private javax.swing.JTable tbl_expenses;
    private javax.swing.JTable tbl_expenses_categories;
    private javax.swing.JTable tbl_expenses_detail;
    private javax.swing.JTable tbl_payment_methods;
    private javax.swing.JTextArea txta_expenses_description;
    private javax.swing.JTextArea txta_expenses_detail_item;
    private javax.swing.JTextField txtf_categories_id;
    private javax.swing.JTextField txtf_expenses_amount;
    private javax.swing.JTextField txtf_expenses_cost;
    private javax.swing.JTextField txtf_expenses_date;
    private javax.swing.JTextField txtf_expenses_detail_amount;
    private javax.swing.JTextField txtf_expenses_detail_cost;
    private javax.swing.JTextField txtf_expenses_detail_expenses_id;
    private javax.swing.JTextField txtf_expenses_detail_id;
    private javax.swing.JTextField txtf_expenses_detail_search;
    private javax.swing.JTextField txtf_expenses_id;
    private javax.swing.JTextField txtf_expenses_search;
    private javax.swing.JTextField txtf_payment_methods_id;
    // End of variables declaration
}
