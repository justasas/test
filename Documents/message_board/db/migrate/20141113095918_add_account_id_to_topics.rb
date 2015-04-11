class AddAccountIdToTopics < ActiveRecord::Migration
  def change
    add_column :topics, :account_id, :integer
  end
end
