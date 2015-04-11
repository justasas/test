class AddCategoryIdToForums < ActiveRecord::Migration
  def change
    add_column :forums, :category_id, :integer
  end
end
