class CreateForums < ActiveRecord::Migration
  def change
    create_table :forums do |t|
      t.string :title
      t.text :description
      t.integer :topic_id
      t.integer :account_id
      t.timestamps
    end
  end
end
