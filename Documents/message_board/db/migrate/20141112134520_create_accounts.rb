class CreateAccounts < ActiveRecord::Migration
  def change
    create_table :accounts do |t|
      t.string :name
      t.string :pass
      t.string :email
      t.integer :birth_year
      t.string :privilege

      t.timestamps
    end
  end
end
