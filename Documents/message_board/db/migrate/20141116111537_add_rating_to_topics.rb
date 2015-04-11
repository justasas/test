class AddRatingToTopics < ActiveRecord::Migration
  def change
    create_table :ratings do |t|
      t.belongs_to :topic
      t.belongs_to :account
      t.integer :rating
      t.timestamps
    end
  end
end
