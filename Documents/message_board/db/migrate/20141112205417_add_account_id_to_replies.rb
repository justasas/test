class AddAccountIdToReplies < ActiveRecord::Migration
  def change
    add_column :replies, :account_id, :integer
  end
end
